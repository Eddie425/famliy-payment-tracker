package com.eddie.famliy_payment_tracker.service;

import com.eddie.famliy_payment_tracker.dto.InstallmentResponseDTO;
import com.eddie.famliy_payment_tracker.dto.UpdateInstallmentRequest;
import com.eddie.famliy_payment_tracker.model.Debt;
import com.eddie.famliy_payment_tracker.model.DebtInstallment;
import com.eddie.famliy_payment_tracker.repository.DebtInstallmentRepository;
import com.eddie.famliy_payment_tracker.repository.DebtRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing debt installments
 */
@Service
@Slf4j
public class InstallmentService {
    
    private final DebtInstallmentRepository installmentRepository;
    private final DebtRepository debtRepository;
    private final DebtService debtService;
    
    public InstallmentService(DebtInstallmentRepository installmentRepository,
                              DebtRepository debtRepository,
                              @Lazy DebtService debtService) {
        this.installmentRepository = installmentRepository;
        this.debtRepository = debtRepository;
        this.debtService = debtService;
    }
    
    /**
     * Generate installments for a debt
     * If monthlyPaymentAmount is provided, use it; otherwise, divide totalAmount equally
     */
    @Transactional
    public void generateInstallments(Debt debt, Long monthlyPaymentAmount) {
        int count = debt.getInstallmentCount();
        Long amountPerInstallment = monthlyPaymentAmount != null
                ? monthlyPaymentAmount
                : debt.getTotalAmount() / count;
        
        // Calculate remainder to add to the last installment
        Long remainder = debt.getTotalAmount() - (amountPerInstallment * count);
        
        LocalDate dueDate = debt.getStartDate();
        
        for (int i = 1; i <= count; i++) {
            Long installmentAmount = (i == count) 
                    ? amountPerInstallment + remainder  // Add remainder to last installment
                    : amountPerInstallment;
            
            DebtInstallment installment = DebtInstallment.builder()
                    .debt(debt)
                    .installmentNumber(i)
                    .amount(installmentAmount)
                    .dueDate(dueDate)
                    .paid(false)
                    .build();
            
            installmentRepository.save(installment);
            dueDate = dueDate.plusMonths(1);
        }
        
        log.info("Generated {} installments for debt ID: {}", count, debt.getId());
    }
    
    /**
     * Get all installments for a specific debt
     */
    public List<InstallmentResponseDTO> getInstallmentsByDebtId(Long debtId) {
        List<DebtInstallment> installments = installmentRepository.findByDebtIdOrderByInstallmentNumberAsc(debtId);
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found with ID: " + debtId));
        
        LocalDate today = LocalDate.now();
        
        return installments.stream()
                .map(installment -> toDTO(installment, debt.getTitle(), today))
                .collect(Collectors.toList());
    }
    
    /**
     * Update an installment (amount and/or due date)
     */
    @Transactional
    public InstallmentResponseDTO updateInstallment(Long id, UpdateInstallmentRequest request) {
        DebtInstallment installment = installmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Installment not found with ID: " + id));
        
        if (request.getAmount() != null && request.getAmount() > 0) {
            installment.setAmount(request.getAmount());
        }
        
        if (request.getDueDate() != null) {
            installment.setDueDate(request.getDueDate());
        }
        
        installment = installmentRepository.save(installment);
        
        Debt debt = installment.getDebt();
        LocalDate today = LocalDate.now();
        
        log.info("Updated installment ID: {} for debt ID: {}", id, debt.getId());
        return toDTO(installment, debt.getTitle(), today);
    }
    
    /**
     * Mark an installment as paid
     */
    @Transactional
    public InstallmentResponseDTO markPaid(Long id) {
        DebtInstallment installment = installmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Installment not found with ID: " + id));
        
        if (installment.getPaid()) {
            throw new RuntimeException("Installment is already marked as paid");
        }
        
        installment.setPaid(true);
        installment.setPaidAt(LocalDate.now());
        
        installment = installmentRepository.save(installment);
        
        // Refresh debt status in case all installments are now paid
        debtService.refreshStatus(installment.getDebt().getId());
        
        Debt debt = installment.getDebt();
        LocalDate today = LocalDate.now();
        
        log.info("Marked installment ID: {} as paid for debt ID: {}", id, debt.getId());
        return toDTO(installment, debt.getTitle(), today);
    }
    
    /**
     * Count unpaid installments for a debt
     */
    public long countUnpaidInstallments(Long debtId) {
        return installmentRepository.countByDebtIdAndPaidFalse(debtId);
    }
    
    /**
     * Count paid installments for a debt
     */
    public long countPaidInstallments(Long debtId) {
        return installmentRepository.countByDebtIdAndPaidTrue(debtId);
    }
    
    /**
     * Calculate total paid amount for a debt
     */
    public long calculatePaidAmount(Long debtId) {
        List<DebtInstallment> paidInstallments = installmentRepository.findByDebtIdAndPaidTrueOrderByInstallmentNumberAsc(debtId);
        return paidInstallments.stream()
                .mapToLong(DebtInstallment::getAmount)
                .sum();
    }
    
    /**
     * Convert DebtInstallment entity to DTO
     */
    private InstallmentResponseDTO toDTO(DebtInstallment installment, String debtTitle, LocalDate today) {
        boolean isOverdue = !installment.getPaid() && installment.getDueDate().isBefore(today);
        
        return InstallmentResponseDTO.builder()
                .id(installment.getId())
                .debtId(installment.getDebt().getId())
                .debtTitle(debtTitle)
                .installmentNumber(installment.getInstallmentNumber())
                .amount(installment.getAmount())
                .dueDate(installment.getDueDate())
                .paid(installment.getPaid())
                .paidAt(installment.getPaidAt())
                .isOverdue(isOverdue)
                .createdAt(installment.getCreatedAt().toLocalDate())
                .updatedAt(installment.getUpdatedAt().toLocalDate())
                .build();
    }
}





