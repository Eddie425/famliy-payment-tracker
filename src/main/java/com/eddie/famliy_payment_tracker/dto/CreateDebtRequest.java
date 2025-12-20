package com.eddie.famliy_payment_tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating a new debt
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new debt with automatic installment generation")
public class CreateDebtRequest {
    
    @Schema(description = "Title/name of the debt (e.g., 'Car Loan', 'Credit Card')", example = "Car Loan", required = true, maxLength = 200)
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    @Schema(description = "Total amount of the debt in smallest currency unit (e.g., cents)", example = "300000", required = true)
    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private Long totalAmount;
    
    @Schema(description = "Number of installments (monthly payments)", example = "12", required = true, minimum = "1")
    @NotNull(message = "Installment count is required")
    @Min(value = 1, message = "Installment count must be at least 1")
    private Integer installmentCount;
    
    @Schema(description = "Start date for the first installment (ISO format: YYYY-MM-DD)", example = "2024-01-01", required = true)
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @Schema(description = "Annual interest rate (optional, as percentage)", example = "5.5")
    @DecimalMin(value = "0.0", message = "Interest rate must be non-negative")
    private BigDecimal interestRate; // Optional
}




