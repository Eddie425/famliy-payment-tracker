package com.eddie.famliy_payment_tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a debt installment (分期明細)
 */
@Entity
@Table(name = "debt_installments", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"debt_id", "installment_number"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebtInstallment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debt_id", nullable = false)
    private Debt debt;
    
    @Column(name = "installment_number", nullable = false)
    private Integer installmentNumber;
    
    @Column(nullable = false)
    private Long amount;
    
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean paid = false;
    
    @Column(name = "paid_at")
    private LocalDate paidAt;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}



