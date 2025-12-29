package com.eddie.famliy_payment_tracker.repository;

import com.eddie.famliy_payment_tracker.model.Debt;
import com.eddie.famliy_payment_tracker.model.DebtStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Debt entities
 */
@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
    
    /**
     * Find all debts by status
     */
    List<Debt> findByStatus(DebtStatus status);
    
    /**
     * Find debt by ID with installments eagerly loaded
     */
    @Query("SELECT d FROM Debt d LEFT JOIN FETCH d.installments WHERE d.id = :id")
    Optional<Debt> findByIdWithInstallments(@Param("id") Long id);
    
    /**
     * Find all debts with installments eagerly loaded
     */
    @Query("SELECT DISTINCT d FROM Debt d LEFT JOIN FETCH d.installments")
    List<Debt> findAllWithInstallments();
    
    /**
     * Find all debts by status with installments eagerly loaded
     */
    @Query("SELECT DISTINCT d FROM Debt d LEFT JOIN FETCH d.installments WHERE d.status = :status")
    List<Debt> findByStatusWithInstallments(@Param("status") DebtStatus status);
}



