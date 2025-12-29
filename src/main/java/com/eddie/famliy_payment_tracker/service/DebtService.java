package com.eddie.famliy_payment_tracker.service;

import com.eddie.famliy_payment_tracker.dto.CreateDebtRequest;
import com.eddie.famliy_payment_tracker.dto.DebtResponseDTO;
import com.eddie.famliy_payment_tracker.model.Debt;
import com.eddie.famliy_payment_tracker.model.DebtStatus;
import com.eddie.famliy_payment_tracker.repository.DebtRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing debts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DebtService {
    
    private final DebtRepository debtRepository;
    private final InstallmentService installmentService;
    
    /**
     * Create a new debt and automatically generate installments
     */
    @Transactional
    public DebtResponseDTO createDebt(CreateDebtRequest request) {
        // Calculate totalAmount from monthlyPaymentAmount if provided
        Long totalAmount = request.getTotalAmount();
        Long monthlyAmount = request.getMonthlyPaymentAmount();
        if (totalAmount == null && monthlyAmount != null) {
            totalAmount = monthlyAmount * request.getInstallmentCount();
        }
        
        // Create Debt entity
        Debt debt = Debt.builder()
                .title(request.getTitle())
                .totalAmount(totalAmount)
                .installmentCount(request.getInstallmentCount())
                .startDate(request.getStartDate())
                .interestRate(request.getInterestRate())
                .status(DebtStatus.ACTIVE)
                .build();
        
        debt = debtRepository.save(debt);
        
        // Generate installments
        installmentService.generateInstallments(debt, monthlyAmount);
        
        // Refresh to load installments
        debt = debtRepository.findByIdWithInstallments(debt.getId())
                .orElseThrow(() -> new RuntimeException("Failed to load created debt"));
        
        log.info("Created debt with ID: {}, title: {}", debt.getId(), debt.getTitle());
        return toDTO(debt, true);
    }
    
    /**
     * Get all debts with optional filtering
     */
    public List<DebtResponseDTO> getAllDebts(String status, Boolean includeInstallments) {
        List<Debt> debts;
        
        if (status != null && !status.isEmpty()) {
            DebtStatus debtStatus = DebtStatus.valueOf(status.toUpperCase());
            debts = includeInstallments 
                    ? debtRepository.findByStatusWithInstallments(debtStatus)
                    : debtRepository.findByStatus(debtStatus);
        } else {
            debts = includeInstallments
                    ? debtRepository.findAllWithInstallments()
                    : debtRepository.findAll();
        }
        
        return debts.stream()
                .map(debt -> toDTO(debt, includeInstallments))
                .collect(Collectors.toList());
    }
    
    /**
     * Get a specific debt by ID
     */
    public DebtResponseDTO getDebtById(Long id, Boolean includeInstallments) {
        Debt debt = includeInstallments
                ? debtRepository.findByIdWithInstallments(id)
                        .orElseThrow(() -> new RuntimeException("Debt not found with ID: " + id))
                : debtRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Debt not found with ID: " + id));
        
        return toDTO(debt, includeInstallments);
    }
    
    /**
     * Delete a debt (cascades to installments)
     */
    @Transactional
    public void deleteDebt(Long id) {
        if (!debtRepository.existsById(id)) {
            throw new RuntimeException("Debt not found with ID: " + id);
        }
        debtRepository.deleteById(id);
        log.info("Deleted debt with ID: {}", id);
    }
    
    /**
     * Refresh debt status - check if all installments are paid and update status to PAID_OFF
     */
    @Transactional
    public void refreshStatus(Long debtId) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found with ID: " + debtId));
        
        long unpaidCount = installmentService.countUnpaidInstallments(debtId);
        
        if (unpaidCount == 0 && debt.getStatus() == DebtStatus.ACTIVE) {
            debt.setStatus(DebtStatus.PAID_OFF);
            debtRepository.save(debt);
            log.info("Updated debt ID: {} status to PAID_OFF", debtId);
        }
    }
    
    /**
     * Convert Debt entity to DTO
     */
    private DebtResponseDTO toDTO(Debt debt, Boolean includeInstallments) {
        DebtResponseDTO.DebtSummaryDTO summary = null;
        
        // Calculate summary if needed
        if (includeInstallments) {
            long paidAmount = installmentService.calculatePaidAmount(debt.getId());
            long remainingAmount = debt.getTotalAmount() - paidAmount;
            int paidCount = (int) installmentService.countPaidInstallments(debt.getId());
            int remainingCount = debt.getInstallmentCount() - paidCount;
            
            summary = DebtResponseDTO.DebtSummaryDTO.builder()
                    .paidAmount(paidAmount)
                    .remainingAmount(remainingAmount)
                    .paidInstallmentsCount(paidCount)
                    .remainingInstallmentsCount(remainingCount)
                    .build();
        }
        
        DebtResponseDTO.DebtResponseDTOBuilder builder = DebtResponseDTO.builder()
                .id(debt.getId())
                .title(debt.getTitle())
                .totalAmount(debt.getTotalAmount())
                .installmentCount(debt.getInstallmentCount())
                .startDate(debt.getStartDate())
                .interestRate(debt.getInterestRate())
                .status(debt.getStatus().name())
                .createdAt(debt.getCreatedAt())
                .updatedAt(debt.getUpdatedAt())
                .summary(summary);
        
        if (includeInstallments) {
            builder.installments(installmentService.getInstallmentsByDebtId(debt.getId()));
        }
        
        return builder.build();
    }
}



