package com.eddie.famliy_payment_tracker.service;

import com.eddie.famliy_payment_tracker.dto.DashboardSummaryDTO;
import com.eddie.famliy_payment_tracker.model.Debt;
import com.eddie.famliy_payment_tracker.model.DebtInstallment;
import com.eddie.famliy_payment_tracker.model.DebtStatus;
import com.eddie.famliy_payment_tracker.repository.DebtInstallmentRepository;
import com.eddie.famliy_payment_tracker.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for dashboard statistics and aggregations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    
    private final DebtRepository debtRepository;
    private final DebtInstallmentRepository installmentRepository;
    
    /**
     * Calculate comprehensive dashboard summary
     */
    public DashboardSummaryDTO calculateSummary(Integer year, Integer month) {
        List<Debt> allDebts = debtRepository.findAllWithInstallments();
        
        // Filter active debts for calculations
        List<Debt> activeDebts = allDebts.stream()
                .filter(debt -> debt.getStatus() == DebtStatus.ACTIVE)
                .collect(Collectors.toList());
        
        // Calculate total statistics
        long totalPaid = 0;
        long totalOutstanding = 0;
        
        for (Debt debt : activeDebts) {
            List<DebtInstallment> installments = debt.getInstallments();
            if (installments != null) {
                for (DebtInstallment installment : installments) {
                    if (installment.getPaid()) {
                        totalPaid += installment.getAmount();
                    } else {
                        totalOutstanding += installment.getAmount();
                    }
                }
            }
        }
        
        long totalAmount = totalPaid + totalOutstanding;
        BigDecimal progressPercentage = totalAmount > 0
                ? BigDecimal.valueOf(totalPaid)
                        .divide(BigDecimal.valueOf(totalAmount), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        
        int activeDebtsCount = (int) allDebts.stream()
                .filter(debt -> debt.getStatus() == DebtStatus.ACTIVE)
                .count();
        int completedDebtsCount = (int) allDebts.stream()
                .filter(debt -> debt.getStatus() == DebtStatus.PAID_OFF)
                .count();
        
        // Build summary info
        DashboardSummaryDTO.SummaryInfo summaryInfo = DashboardSummaryDTO.SummaryInfo.builder()
                .totalPaid(totalPaid)
                .totalOutstanding(totalOutstanding)
                .totalAmount(totalAmount)
                .progressPercentage(progressPercentage)
                .activeDebtsCount(activeDebtsCount)
                .completedDebtsCount(completedDebtsCount)
                .build();
        
        // Calculate monthly breakdown
        List<DashboardSummaryDTO.MonthlyBreakdownDTO> monthlyBreakdown = calculateMonthlyBreakdowns(activeDebts);
        
        // Calculate debt breakdown
        List<DashboardSummaryDTO.DebtBreakdownDTO> debtBreakdown = calculateDebtBreakdowns(activeDebts);
        
        // Build visualization data
        DashboardSummaryDTO.VisualizationDataDTO visualizationData = DashboardSummaryDTO.VisualizationDataDTO.builder()
                .chartData(DashboardSummaryDTO.ChartDataDTO.builder()
                        .labels(Arrays.asList("Paid", "Remaining"))
                        .values(Arrays.asList(totalPaid, totalOutstanding))
                        .colors(Arrays.asList("#10b981", "#ef4444"))
                        .build())
                .progressBarData(DashboardSummaryDTO.ProgressBarDataDTO.builder()
                        .current(totalPaid)
                        .total(totalAmount)
                        .percentage(progressPercentage)
                        .build())
                .build();
        
        return DashboardSummaryDTO.builder()
                .summary(summaryInfo)
                .monthlyBreakdown(monthlyBreakdown)
                .debtBreakdown(debtBreakdown)
                .visualizationData(visualizationData)
                .build();
    }
    
    /**
     * Calculate monthly breakdown
     */
    public DashboardSummaryDTO.MonthlyBreakdownDTO calculateMonthlyBreakdown(Integer year, Integer month) {
        LocalDate targetDate = LocalDate.of(year, month, 1);
        LocalDate endDate = targetDate.plusMonths(1).minusDays(1);
        
        // Get installments with their debts eagerly loaded
        List<DebtInstallment> installments = installmentRepository.findByDueDateBetween(targetDate, endDate);
        
        // Filter for active debts only
        List<DebtInstallment> activeInstallments = installments.stream()
                .filter(i -> i.getDebt() != null && i.getDebt().getStatus() == DebtStatus.ACTIVE)
                .collect(Collectors.toList());
        
        long totalDue = activeInstallments.stream()
                .mapToLong(DebtInstallment::getAmount)
                .sum();
        
        long totalPaid = activeInstallments.stream()
                .filter(DebtInstallment::getPaid)
                .mapToLong(DebtInstallment::getAmount)
                .sum();
        
        long remaining = totalDue - totalPaid;
        boolean isComplete = activeInstallments.stream().allMatch(DebtInstallment::getPaid);
        
        List<DashboardSummaryDTO.InstallmentDetailDTO> installmentDetails = activeInstallments.stream()
                .map(this::toInstallmentDetailDTO)
                .sorted(Comparator.comparing(DashboardSummaryDTO.InstallmentDetailDTO::getDueDate))
                .collect(Collectors.toList());
        
        String monthStr = String.format("%04d-%02d", year, month);
        String monthLabel = targetDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        
        return DashboardSummaryDTO.MonthlyBreakdownDTO.builder()
                .month(monthStr)
                .monthLabel(monthLabel)
                .totalDue(totalDue)
                .totalPaid(totalPaid)
                .remaining(remaining)
                .isComplete(isComplete)
                .installments(installmentDetails)
                .build();
    }
    
    /**
     * Calculate monthly breakdowns for current and upcoming months
     */
    private List<DashboardSummaryDTO.MonthlyBreakdownDTO> calculateMonthlyBreakdowns(List<Debt> activeDebts) {
        List<DashboardSummaryDTO.MonthlyBreakdownDTO> monthlyList = new ArrayList<>();
        
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        
        // Current month
        monthlyList.add(calculateMonthlyBreakdown(currentYear, currentMonth));
        
        // Next month
        LocalDate nextMonth = now.plusMonths(1);
        monthlyList.add(calculateMonthlyBreakdown(nextMonth.getYear(), nextMonth.getMonthValue()));
        
        return monthlyList;
    }
    
    /**
     * Calculate debt breakdown (progress per debt)
     */
    private List<DashboardSummaryDTO.DebtBreakdownDTO> calculateDebtBreakdowns(List<Debt> activeDebts) {
        return activeDebts.stream()
                .map(debt -> {
                    List<DebtInstallment> installments = debt.getInstallments();
                    long paidAmount = (installments != null) 
                            ? installments.stream()
                                    .filter(DebtInstallment::getPaid)
                                    .mapToLong(DebtInstallment::getAmount)
                                    .sum()
                            : 0L;
                    
                    long remainingAmount = debt.getTotalAmount() - paidAmount;
                    BigDecimal progressPercentage = debt.getTotalAmount() > 0
                            ? BigDecimal.valueOf(paidAmount)
                                    .divide(BigDecimal.valueOf(debt.getTotalAmount()), 4, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100))
                                    .setScale(2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;
                    
                    return DashboardSummaryDTO.DebtBreakdownDTO.builder()
                            .debtId(debt.getId())
                            .title(debt.getTitle())
                            .totalAmount(debt.getTotalAmount())
                            .paidAmount(paidAmount)
                            .remainingAmount(remainingAmount)
                            .progressPercentage(progressPercentage)
                            .status(debt.getStatus().name())
                            .build();
                })
                .sorted(Comparator.comparing(DashboardSummaryDTO.DebtBreakdownDTO::getTitle))
                .collect(Collectors.toList());
    }
    
    /**
     * Convert DebtInstallment to InstallmentDetailDTO
     */
    private DashboardSummaryDTO.InstallmentDetailDTO toInstallmentDetailDTO(DebtInstallment installment) {
        LocalDate today = LocalDate.now();
        boolean isOverdue = !installment.getPaid() && installment.getDueDate().isBefore(today);
        
        String debtTitle = installment.getDebt() != null ? installment.getDebt().getTitle() : "Unknown";
        
        return DashboardSummaryDTO.InstallmentDetailDTO.builder()
                .installmentId(installment.getId())
                .debtTitle(debtTitle)
                .amount(installment.getAmount())
                .dueDate(installment.getDueDate().toString())
                .paid(installment.getPaid())
                .paidAt(installment.getPaidAt() != null ? installment.getPaidAt().toString() : null)
                .isOverdue(isOverdue)
                .build();
    }
}



