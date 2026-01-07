package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing financial payment records.
 * <p>
 * This interface defines the contract for handling the lifecycle of {@link Payment} entities,
 * including retrieval, creation, deletion, and status updates (e.g., approving a payment).
 * </p>
 */
@Repository
public interface PaymentRepository {

    /**
     * Retrieves the complete history of all payments in the system.
     *
     * @return a {@link List} of all {@link Payment} entities
     */
    List<Payment> findAll();

    /**
     * Retrieves the complete history of all my payments in the system.
     *
     * @return a {@link List} of all {@link Payment} entities
     */
    List<Payment> findAllMine(Long userId);

    /**
     * Creates a new payment record.
     * <p>
     * <i>Note: Implementation details (parameters) are pending.</i>
     * </p>
     */
    void createPayment();

    /**
     * Deletes a specific payment record from the history.
     *
     * @param id the unique identifier of the payment to delete
     */
    void deletePayment(Long id);

    /**
     * Edits the details of an existing payment.
     * <p>
     * <i>Note: Implementation details (parameters) are pending.</i>
     * </p>
     */
    void editPayment();

    /**
     * Updates the status of a specific payment.
     * <p>
     * This is typically used to transition a payment through its workflow
     * (e.g., from "Pending" to "Paid").
     * </p>
     *
     * @param id       the unique identifier of the payment record
     * @param statusId the unique identifier of the new status to apply
     */
    void updateStatus(Long id, Long statusId);
}