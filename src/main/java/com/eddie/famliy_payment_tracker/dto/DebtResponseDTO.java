package com.eddie.famliy_payment_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for debt response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebtResponseDTO {
    
    private Long id;
    private String title;
    private Long totalAmount;
    private Integer installmentCount;
    private LocalDate startDate;
    private BigDecimal interestRate;
    private String status; // "ACTIVE" or "PAID_OFF"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Optional: Include installments if requested
    private List<InstallmentResponseDTO> installments;
    
    // Optional: Summary statistics
    private DebtSummaryDTO summary;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DebtSummaryDTO {
        private Long paidAmount;
        private Long remainingAmount;
        private Integer paidInstallmentsCount;
        private Integer remainingInstallmentsCount;
    }
}















