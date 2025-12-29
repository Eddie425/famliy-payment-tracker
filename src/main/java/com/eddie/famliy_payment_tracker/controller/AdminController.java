package com.eddie.famliy_payment_tracker.controller;

import com.eddie.famliy_payment_tracker.dto.*;
import com.eddie.famliy_payment_tracker.service.DebtService;
import com.eddie.famliy_payment_tracker.service.InstallmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin controller for managing debts and installments
 * All endpoints are under /api/admin/** for future permission control
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin APIs", description = "APIs for managing debts and adjusting monthly payment installments")
@RequiredArgsConstructor
public class AdminController {
    
    private final DebtService debtService;
    private final InstallmentService installmentService;

    /**
     * Create a new debt and automatically generate installments
     * POST /api/admin/debts
     * 
     * @param request Debt creation request
     * @return Created debt with installments
     */
    @Operation(
            summary = "Create a new debt",
            description = "Creates a new debt and automatically generates installments based on the installment count. " +
                    "The installments will be evenly distributed across the payment period starting from the start date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Debt created successfully",
                    content = @Content(schema = @Schema(implementation = DebtResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/debts")
    public ResponseEntity<DebtResponseDTO> createDebt(
            @Parameter(description = "Debt creation request with title, total amount (or monthly payment amount), installment count, and start date")
            @Valid @RequestBody CreateDebtRequest request) {
        DebtResponseDTO response = debtService.createDebt(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all debts with optional filtering
     * GET /api/admin/debts?status=ACTIVE&includeInstallments=true
     * 
     * @param status Optional filter by status (ACTIVE, PAID_OFF)
     * @param includeInstallments Whether to include installment details
     * @return List of debts
     */
    @Operation(
            summary = "Get all debts",
            description = "Retrieves all debts with optional filtering by status. " +
                    "Can optionally include installment details for each debt."
    )
    @ApiResponse(responseCode = "200", description = "List of debts retrieved successfully")
    @GetMapping("/debts")
    public ResponseEntity<List<DebtResponseDTO>> getAllDebts(
            @Parameter(description = "Filter by debt status: ACTIVE or PAID_OFF")
            @RequestParam(required = false) String status,
            @Parameter(description = "Whether to include installment details in the response")
            @RequestParam(defaultValue = "false") Boolean includeInstallments) {
        List<DebtResponseDTO> debts = debtService.getAllDebts(status, includeInstallments);
        return ResponseEntity.ok(debts);
    }

    /**
     * Get a specific debt by ID
     * GET /api/admin/debts/{id}
     * 
     * @param id Debt ID
     * @param includeInstallments Whether to include installment details
     * @return Debt details
     */
    @Operation(
            summary = "Get debt by ID",
            description = "Retrieves detailed information about a specific debt by its ID. " +
                    "Includes summary statistics (paid amount, remaining amount, etc.)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Debt found",
                    content = @Content(schema = @Schema(implementation = DebtResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Debt not found")
    })
    @GetMapping("/debts/{id}")
    public ResponseEntity<DebtResponseDTO> getDebtById(
            @Parameter(description = "ID of the debt to retrieve")
            @PathVariable Long id,
            @Parameter(description = "Whether to include installment details")
            @RequestParam(defaultValue = "true") Boolean includeInstallments) {
        DebtResponseDTO debt = debtService.getDebtById(id, includeInstallments);
        return ResponseEntity.ok(debt);
    }

    /**
     * Get all installments for a specific debt
     * GET /api/admin/debts/{id}/installments
     * 
     * @param id Debt ID
     * @return List of installments for the debt
     */
    @Operation(
            summary = "Get installments for a debt",
            description = "Retrieves all installments (monthly payments) for a specific debt, " +
                    "sorted by installment number. Shows payment status, due dates, and overdue indicators."
    )
    @ApiResponse(responseCode = "200", description = "List of installments retrieved successfully")
    @GetMapping("/debts/{id}/installments")
    public ResponseEntity<List<InstallmentResponseDTO>> getDebtInstallments(
            @Parameter(description = "ID of the debt")
            @PathVariable Long id) {
        List<InstallmentResponseDTO> installments = installmentService.getInstallmentsByDebtId(id);
        return ResponseEntity.ok(installments);
    }

    /**
     * Update an installment (amount and/or due date)
     * PUT /api/admin/installments/{id}
     * 
     * This is the main endpoint for adjusting monthly payments
     * 
     * @param id Installment ID
     * @param request Update request (amount and/or dueDate)
     * @return Updated installment
     */
    @Operation(
            summary = "Update installment (Adjust Monthly Payment)",
            description = "Updates the amount and/or due date of a specific installment. " +
                    "This is the main endpoint for adjusting monthly payments. " +
                    "Both amount and dueDate are optional - only provided fields will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Installment updated successfully",
                    content = @Content(schema = @Schema(implementation = InstallmentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Installment not found")
    })
    @PutMapping("/installments/{id}")
    public ResponseEntity<InstallmentResponseDTO> updateInstallment(
            @Parameter(description = "ID of the installment to update")
            @PathVariable Long id,
            @Parameter(description = "Update request with optional amount and/or dueDate")
            @Valid @RequestBody UpdateInstallmentRequest request) {
        InstallmentResponseDTO updated = installmentService.updateInstallment(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Bulk update multiple installments
     * PUT /api/admin/installments/bulk
     * 
     * Useful for adjusting multiple payments at once
     * 
     * @param requests List of updates (each with installmentId, amount, dueDate)
     * @return List of updated installments
     */
    @Operation(
            summary = "Bulk update installments",
            description = "Updates multiple installments in a single request. " +
                    "Useful for adjusting an entire payment schedule at once. " +
                    "Each request must include the installmentId and optionally amount and/or dueDate."
    )
    @ApiResponse(responseCode = "200", description = "Installments updated successfully")
    @PutMapping("/installments/bulk")
    public ResponseEntity<List<InstallmentResponseDTO>> bulkUpdateInstallments(
            @Parameter(description = "List of installment updates")
            @Valid @RequestBody List<BulkUpdateInstallmentRequest> requests) {
        List<InstallmentResponseDTO> updated = new ArrayList<>();
        for (BulkUpdateInstallmentRequest req : requests) {
            UpdateInstallmentRequest updateRequest = UpdateInstallmentRequest.builder()
                    .amount(req.getAmount())
                    .dueDate(req.getDueDate())
                    .build();
            updated.add(installmentService.updateInstallment(req.getInstallmentId(), updateRequest));
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a debt (or mark as inactive)
     * DELETE /api/admin/debts/{id}
     * 
     * @param id Debt ID
     * @return Success message
     */
    @Operation(
            summary = "Delete a debt",
            description = "Deletes a debt and all its associated installments. " +
                    "This action cannot be undone. Consider marking as inactive instead if you want to preserve history."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Debt deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Debt not found")
    })
    @DeleteMapping("/debts/{id}")
    public ResponseEntity<ApiResponseMessage> deleteDebt(
            @Parameter(description = "ID of the debt to delete")
            @PathVariable Long id) {
        debtService.deleteDebt(id);
        return ResponseEntity.ok(ApiResponseMessage.builder()
                .success(true)
                .message("Debt deleted successfully")
                .build());
    }

    // ========== Sample Data Methods (to be replaced with actual service calls) ==========
    
    private DebtResponseDTO createSampleDebtResponse(CreateDebtRequest request, Long calculatedTotalAmount, Long monthlyAmount) {
        // Use monthlyAmount for installments if provided, otherwise calculate from totalAmount
        Long installmentAmount = monthlyAmount != null 
            ? monthlyAmount 
            : (request.getTotalAmount() != null ? request.getTotalAmount() / request.getInstallmentCount() : 25000L);
        
        DebtResponseDTO response = DebtResponseDTO.builder()
                .id(1L)
                .title(request.getTitle())
                .totalAmount(calculatedTotalAmount)
                .installmentCount(request.getInstallmentCount())
                .startDate(request.getStartDate())
                .interestRate(request.getInterestRate())
                .status("ACTIVE")
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();
        
        // Generate installments with the calculated monthly amount
        List<InstallmentResponseDTO> installments = new ArrayList<>();
        LocalDate dueDate = request.getStartDate();
        for (int i = 1; i <= request.getInstallmentCount(); i++) {
            installments.add(InstallmentResponseDTO.builder()
                    .id((long) i)
                    .debtId(1L)
                    .debtTitle(request.getTitle())
                    .installmentNumber(i)
                    .amount(installmentAmount)
                    .dueDate(dueDate)
                    .paid(false)
                    .isOverdue(false)
                    .build());
            dueDate = dueDate.plusMonths(1);
        }
        response.setInstallments(installments);
        
        return response;
    }
    
    private List<DebtResponseDTO> createSampleDebtsList(boolean includeInstallments) {
        List<DebtResponseDTO> debts = new ArrayList<>();
        
        // Use current date for more realistic sample data
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusMonths(3).withDayOfMonth(1);
        
        DebtResponseDTO debt1 = DebtResponseDTO.builder()
                .id(1L)
                .title("Car Loan")
                .totalAmount(300000L)
                .installmentCount(12)
                .startDate(startDate)
                .status("ACTIVE")
                .summary(DebtResponseDTO.DebtSummaryDTO.builder()
                        .paidAmount(90000L)
                        .remainingAmount(210000L)
                        .paidInstallmentsCount(3)
                        .remainingInstallmentsCount(9)
                        .build())
                .build();
        
        if (includeInstallments) {
            debt1.setInstallments(createSampleInstallments(1L, startDate));
        }
        
        debts.add(debt1);
        return debts;
    }
    
    private DebtResponseDTO createSampleDebtResponse(Long id, boolean includeInstallments) {
        // Use current date for more realistic sample data
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusMonths(3).withDayOfMonth(1);
        
        DebtResponseDTO debt = DebtResponseDTO.builder()
                .id(id)
                .title("Car Loan")
                .totalAmount(300000L)
                .installmentCount(12)
                .startDate(startDate)
                .status("ACTIVE")
                .summary(DebtResponseDTO.DebtSummaryDTO.builder()
                        .paidAmount(90000L)
                        .remainingAmount(210000L)
                        .paidInstallmentsCount(3)
                        .remainingInstallmentsCount(9)
                        .build())
                .build();
        
        if (includeInstallments) {
            debt.setInstallments(createSampleInstallments(id, startDate));
        }
        
        return debt;
    }
    
    private List<InstallmentResponseDTO> createSampleInstallments(Long debtId, LocalDate startDate) {
        List<InstallmentResponseDTO> installments = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate firstDueDate = startDate.withDayOfMonth(15);
        
        for (int i = 1; i <= 12; i++) {
            LocalDate dueDate = firstDueDate.plusMonths(i - 1);
            boolean isPaid = dueDate.isBefore(now) || (dueDate.equals(now) && i <= 3);
            
            installments.add(InstallmentResponseDTO.builder()
                    .id((long) i)
                    .debtId(debtId)
                    .debtTitle("Car Loan")
                    .installmentNumber(i)
                    .amount(25000L)
                    .dueDate(dueDate)
                    .paid(isPaid)
                    .paidAt(isPaid ? dueDate.minusDays(5) : null)
                    .isOverdue(!isPaid && dueDate.isBefore(now))
                    .build());
        }
        
        return installments;
    }
    
    private InstallmentResponseDTO createSampleUpdatedInstallment(Long id, UpdateInstallmentRequest request) {
        return InstallmentResponseDTO.builder()
                .id(id)
                .debtId(1L)
                .debtTitle("Car Loan")
                .installmentNumber(4)
                .amount(request.getAmount() != null ? request.getAmount() : 25000L)
                .dueDate(request.getDueDate() != null ? request.getDueDate() : LocalDate.of(2024, 4, 15))
                .paid(false)
                .isOverdue(false)
                .build();
    }
    
    /**
     * DTO for bulk update requests
     */
    public static class BulkUpdateInstallmentRequest {
        @jakarta.validation.constraints.NotNull
        private Long installmentId;
        
        @jakarta.validation.constraints.Positive
        private Long amount;
        
        private LocalDate dueDate;
        
        // Getters and setters
        public Long getInstallmentId() { return installmentId; }
        public void setInstallmentId(Long installmentId) { this.installmentId = installmentId; }
        public Long getAmount() { return amount; }
        public void setAmount(Long amount) { this.amount = amount; }
        public LocalDate getDueDate() { return dueDate; }
        public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    }
    
    /**
     * Generic API response
     */
    public static class ApiResponseMessage {
        private Boolean success;
        private String message;
        
        // Builder pattern
        public static ApiResponseMessageBuilder builder() {
            return new ApiResponseMessageBuilder();
        }
        
        public static class ApiResponseMessageBuilder {
            private Boolean success;
            private String message;
            
            public ApiResponseMessageBuilder success(Boolean success) {
                this.success = success;
                return this;
            }
            
            public ApiResponseMessageBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public ApiResponseMessage build() {
                ApiResponseMessage response = new ApiResponseMessage();
                response.success = this.success;
                response.message = this.message;
                return response;
            }
        }
        
        // Getters
        public Boolean getSuccess() { return success; }
        public String getMessage() { return message; }
    }
}















