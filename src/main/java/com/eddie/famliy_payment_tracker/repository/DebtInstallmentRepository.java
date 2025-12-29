package com.eddie.famliy_payment_tracker.repository;

import com.eddie.famliy_payment_tracker.model.DebtInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for DebtInstallment entities
 */
@Repository
public interface DebtInstallmentRepository extends JpaRepository<DebtInstallment, Long> {
    
    /**
     * Find all installments for a specific debt, ordered by installment number
     */
    List<DebtInstallment> findByDebtIdOrderByInstallmentNumberAsc(Long debtId);
    
    /**
     * Find all unpaid installments for a specific debt
     */
    List<DebtInstallment> findByDebtIdAndPaidFalseOrderByInstallmentNumberAsc(Long debtId);
    
    /**
     * Find all paid installments for a specific debt
     */
    List<DebtInstallment> findByDebtIdAndPaidTrueOrderByInstallmentNumberAsc(Long debtId);
    
    /**
     * Find all unpaid installments with due date before the specified date (overdue)
     */
    @Query("SELECT i FROM DebtInstallment i WHERE i.paid = false AND i.dueDate < :date ORDER BY i.dueDate ASC")
    List<DebtInstallment> findOverdueInstallments(@Param("date") LocalDate date);
    
    /**
     * Find all installments for active debts
     */
    @Query("SELECT i FROM DebtInstallment i WHERE i.debt.status = 'ACTIVE'")
    List<DebtInstallment> findByActiveDebts();
    
    /**
     * Find all unpaid installments for active debts
     */
    @Query("SELECT i FROM DebtInstallment i WHERE i.debt.status = 'ACTIVE' AND i.paid = false ORDER BY i.dueDate ASC")
    List<DebtInstallment> findUnpaidInstallmentsForActiveDebts();
    
    /**
     * Find all paid installments for active debts
     */
    @Query("SELECT i FROM DebtInstallment i WHERE i.debt.status = 'ACTIVE' AND i.paid = true")
    List<DebtInstallment> findPaidInstallmentsForActiveDebts();
    
    /**
     * Count unpaid installments for a specific debt
     */
    long countByDebtIdAndPaidFalse(Long debtId);
    
    /**
     * Count paid installments for a specific debt
     */
    long countByDebtIdAndPaidTrue(Long debtId);
    
    /**
     * Find installment by debt ID and installment number
     */
    Optional<DebtInstallment> findByDebtIdAndInstallmentNumber(Long debtId, Integer installmentNumber);
    
    /**
     * Find all installments by due date range with debt eagerly loaded
     */
    @Query("SELECT i FROM DebtInstallment i LEFT JOIN FETCH i.debt WHERE i.dueDate BETWEEN :startDate AND :endDate ORDER BY i.dueDate ASC")
    List<DebtInstallment> findByDueDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}



