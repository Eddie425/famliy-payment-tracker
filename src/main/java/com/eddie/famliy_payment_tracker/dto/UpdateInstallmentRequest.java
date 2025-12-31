package com.eddie.famliy_payment_tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for updating an installment
 * All fields are optional - only provided fields will be updated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update an installment. All fields are optional - only provided fields will be updated.")
public class UpdateInstallmentRequest {
    
    @Schema(description = "New payment amount in smallest currency unit (e.g., cents). Leave null to keep current amount.", example = "3000000")
    @Positive(message = "Amount must be positive")
    private Long amount; // Optional - only update if provided
    
    @Schema(description = "New due date (ISO format: YYYY-MM-DD). Leave null to keep current due date.", example = "2024-04-20")
    private LocalDate dueDate; // Optional - only update if provided
}

















