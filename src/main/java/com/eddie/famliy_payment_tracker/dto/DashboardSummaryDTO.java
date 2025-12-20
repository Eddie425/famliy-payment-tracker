package com.eddie.famliy_payment_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for dashboard summary response
 * Contains overall payment statistics and breakdowns
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    
    private SummaryInfo summary;
    private List<MonthlyBreakdownDTO> monthlyBreakdown;
    private List<DebtBreakdownDTO> debtBreakdown;
    private VisualizationDataDTO visualizationData;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryInfo {
        private Long totalPaid;           // Total amount paid across all debts
        private Long totalOutstanding;     // Total amount remaining to pay
        private Long totalAmount;          // Total amount (paid + outstanding)
        private BigDecimal progressPercentage; // Percentage of total paid
        private Integer activeDebtsCount;  // Number of active debts
        private Integer completedDebtsCount; // Number of paid-off debts
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyBreakdownDTO {
        private String month;              // Format: "YYYY-MM"
        private String monthLabel;         // Format: "January 2024"
        private Long totalDue;             // Total amount due this month
        private Long totalPaid;            // Total amount paid this month
        private Long remaining;            // Remaining amount this month
        private Boolean isComplete;        // Whether all installments are paid
        private List<InstallmentDetailDTO> installments;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InstallmentDetailDTO {
        private Long installmentId;
        private String debtTitle;
        private Long amount;
        private String dueDate;            // ISO date format
        private Boolean paid;
        private String paidAt;             // ISO date format, null if not paid
        private Boolean isOverdue;         // true if not paid and dueDate < today
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DebtBreakdownDTO {
        private Long debtId;
        private String title;
        private Long totalAmount;
        private Long paidAmount;
        private Long remainingAmount;
        private BigDecimal progressPercentage;
        private String status;             // "ACTIVE" or "PAID_OFF"
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VisualizationDataDTO {
        private ChartDataDTO chartData;
        private ProgressBarDataDTO progressBarData;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartDataDTO {
        private List<String> labels;       // ["Paid", "Remaining"]
        private List<Long> values;         // [150000, 350000]
        private List<String> colors;       // ["#10b981", "#ef4444"]
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProgressBarDataDTO {
        private Long current;              // Current paid amount
        private Long total;                // Total amount
        private BigDecimal percentage;     // Progress percentage
    }
}




