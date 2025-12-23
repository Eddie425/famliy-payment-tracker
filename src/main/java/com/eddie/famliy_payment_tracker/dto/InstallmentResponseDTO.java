package com.eddie.famliy_payment_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for installment response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentResponseDTO {
    
    private Long id;
    private Long debtId;
    private String debtTitle; // For convenience
    private Integer installmentNumber;
    private Long amount;
    private LocalDate dueDate;
    private Boolean paid;
    private LocalDate paidAt;
    private Boolean isOverdue; // Calculated: !paid && dueDate < today
    private LocalDate createdAt;
    private LocalDate updatedAt;
}











