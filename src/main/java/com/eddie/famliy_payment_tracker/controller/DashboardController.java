package com.eddie.famliy_payment_tracker.controller;

import com.eddie.famliy_payment_tracker.dto.DashboardSummaryDTO;
import com.eddie.famliy_payment_tracker.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for dashboard endpoints
 * Provides aggregated payment statistics and visualization data
 */
@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard APIs", description = "APIs for viewing payment statistics, monthly breakdowns, and visualization data")
@RequiredArgsConstructor
public class DashboardController {
    
    private final DashboardService dashboardService;

    /**
     * Get comprehensive dashboard summary
     * Returns overall statistics, monthly breakdown, and visualization data
     * 
     * @param year Optional year filter (default: current year)
     * @param month Optional month filter (default: all months)
     * @return Dashboard summary with all payment information
     */
    @Operation(
            summary = "Get dashboard summary",
            description = "Returns comprehensive payment statistics including: " +
                    "total paid/remaining amounts, monthly breakdown, debt-by-debt progress, " +
                    "and pre-formatted data for charts and progress bars. " +
                    "Perfect for displaying payment overview on a single page."
    )
    @ApiResponse(responseCode = "200", description = "Dashboard summary retrieved successfully",
            content = @Content(schema = @Schema(implementation = DashboardSummaryDTO.class)))
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(
            @Parameter(description = "Filter by year (e.g., 2024)")
            @RequestParam(required = false) Integer year,
            @Parameter(description = "Filter by month (1-12)")
            @RequestParam(required = false) Integer month) {
        DashboardSummaryDTO response = dashboardService.calculateSummary(year, month);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get monthly payment breakdown
     * Returns detailed information for a specific month
     * 
     * @param year Year (required)
     * @param month Month (1-12, required)
     * @return Monthly breakdown details
     */
    @Operation(
            summary = "Get monthly payment breakdown",
            description = "Returns detailed payment information for a specific month, " +
                    "including all installments due that month, payment status, and overdue indicators."
    )
    @ApiResponse(responseCode = "200", description = "Monthly breakdown retrieved successfully")
    @GetMapping("/monthly")
    public ResponseEntity<DashboardSummaryDTO.MonthlyBreakdownDTO> getMonthlyBreakdown(
            @Parameter(description = "Year (e.g., 2024)", required = true)
            @RequestParam Integer year,
            @Parameter(description = "Month (1-12)", required = true)
            @RequestParam Integer month) {
        DashboardSummaryDTO.MonthlyBreakdownDTO monthly = dashboardService.calculateMonthlyBreakdown(year, month);
        return ResponseEntity.ok(monthly);
    }
    
    // Sample data methods - to be replaced with actual service implementation
    private DashboardSummaryDTO createSampleDashboardSummary() {
        long totalPaid = 150000L;
        long totalOutstanding = 350000L;
        long totalAmount = totalPaid + totalOutstanding;
        
        BigDecimal progressPercentage = BigDecimal.valueOf(totalPaid)
                .divide(BigDecimal.valueOf(totalAmount), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
        
        return DashboardSummaryDTO.builder()
                .summary(DashboardSummaryDTO.SummaryInfo.builder()
                        .totalPaid(totalPaid)
                        .totalOutstanding(totalOutstanding)
                        .totalAmount(totalAmount)
                        .progressPercentage(progressPercentage)
                        .activeDebtsCount(3)
                        .completedDebtsCount(1)
                        .build())
                .monthlyBreakdown(createSampleMonthlyBreakdowns())
                .debtBreakdown(createSampleDebtBreakdowns())
                .visualizationData(DashboardSummaryDTO.VisualizationDataDTO.builder()
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
                        .build())
                .build();
    }
    
    private List<DashboardSummaryDTO.MonthlyBreakdownDTO> createSampleMonthlyBreakdowns() {
        List<DashboardSummaryDTO.MonthlyBreakdownDTO> monthly = new ArrayList<>();
        
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        
        // Current month - Partial
        monthly.add(createSampleMonthlyBreakdown(currentYear, currentMonth));
        
        // Next month - Upcoming
        LocalDate nextMonth = now.plusMonths(1);
        monthly.add(createSampleMonthlyBreakdown(nextMonth.getYear(), nextMonth.getMonthValue()));
        
        return monthly;
    }
    
    private DashboardSummaryDTO.MonthlyBreakdownDTO createSampleMonthlyBreakdown(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        String monthStr = String.format("%04d-%02d", year, month);
        String monthLabel = date.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        
        LocalDate now = LocalDate.now();
        boolean isCurrentMonth = (year == now.getYear() && month == now.getMonthValue());
        boolean isPastMonth = date.isBefore(now.withDayOfMonth(1));
        
        if (isPastMonth) {
            // Past month - fully paid
            return DashboardSummaryDTO.MonthlyBreakdownDTO.builder()
                    .month(monthStr)
                    .monthLabel(monthLabel)
                    .totalDue(50000L)
                    .totalPaid(50000L)
                    .remaining(0L)
                    .isComplete(true)
                    .installments(createSampleInstallments(year, month, true))
                    .build();
        } else if (isCurrentMonth) {
            // Current month - partially paid
            return DashboardSummaryDTO.MonthlyBreakdownDTO.builder()
                    .month(monthStr)
                    .monthLabel(monthLabel)
                    .totalDue(50000L)
                    .totalPaid(30000L)
                    .remaining(20000L)
                    .isComplete(false)
                    .installments(createSampleInstallments(year, month, false))
                    .build();
        } else {
            // Future month - not paid yet
            return DashboardSummaryDTO.MonthlyBreakdownDTO.builder()
                    .month(monthStr)
                    .monthLabel(monthLabel)
                    .totalDue(50000L)
                    .totalPaid(0L)
                    .remaining(50000L)
                    .isComplete(false)
                    .installments(createSampleInstallments(year, month, false))
                    .build();
        }
    }
    
    private List<DashboardSummaryDTO.InstallmentDetailDTO> createSampleInstallments(int year, int month, boolean allPaid) {
        List<DashboardSummaryDTO.InstallmentDetailDTO> installments = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate dueDate1 = monthStart.withDayOfMonth(15);
        LocalDate dueDate2 = monthStart.withDayOfMonth(20);
        
        installments.add(DashboardSummaryDTO.InstallmentDetailDTO.builder()
                .installmentId(1L)
                .debtTitle("Car Loan")
                .amount(30000L)
                .dueDate(dueDate1.toString())
                .paid(allPaid || dueDate1.isBefore(now))
                .paidAt((allPaid || dueDate1.isBefore(now)) ? dueDate1.minusDays(5).toString() : null)
                .isOverdue(!allPaid && dueDate1.isBefore(now) && !dueDate1.minusDays(5).isBefore(now))
                .build());
        
        installments.add(DashboardSummaryDTO.InstallmentDetailDTO.builder()
                .installmentId(2L)
                .debtTitle("Credit Card")
                .amount(20000L)
                .dueDate(dueDate2.toString())
                .paid(allPaid)
                .paidAt(allPaid ? dueDate2.minusDays(2).toString() : null)
                .isOverdue(!allPaid && dueDate2.isBefore(now))
                .build());
        
        return installments;
    }
    
    private List<DashboardSummaryDTO.DebtBreakdownDTO> createSampleDebtBreakdowns() {
        List<DashboardSummaryDTO.DebtBreakdownDTO> debts = new ArrayList<>();
        
        debts.add(DashboardSummaryDTO.DebtBreakdownDTO.builder()
                .debtId(1L)
                .title("Car Loan")
                .totalAmount(300000L)
                .paidAmount(90000L)
                .remainingAmount(210000L)
                .progressPercentage(BigDecimal.valueOf(30.0))
                .status("ACTIVE")
                .build());
        
        debts.add(DashboardSummaryDTO.DebtBreakdownDTO.builder()
                .debtId(2L)
                .title("Credit Card")
                .totalAmount(200000L)
                .paidAmount(60000L)
                .remainingAmount(140000L)
                .progressPercentage(BigDecimal.valueOf(30.0))
                .status("ACTIVE")
                .build());
        
        return debts;
    }
}

















