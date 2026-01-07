package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.PaymentStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing payment status definitions.
 * <p>
 * This interface defines operations to maintain the dictionary of possible
 * payment states (e.g., "Pending", "Approved", "Rejected") used to track
 * the lifecycle of financial transactions.
 * </p>
 */
@Repository
public interface PaymentStatusRepository {

    /**
     * Retrieves all available payment statuses defined in the system.
     *
     * @return a {@link List} of all {@link PaymentStatus} entities
     */
    List<PaymentStatus> findAll();

    /**
     * Creates and persists a new payment status definition.
     *
     * @param name        the unique name of the status (e.g., "Cancelled")
     * @param description a brief description of what this status implies
     */
    void createPaymentStatus(String name, String description);

    /**
     * Deletes a payment status definition from the system.
     *
     * @param id the unique identifier of the status to remove
     */
    void deletePaymentStatus(Long id);

    /**
     * Updates the details of an existing payment status.
     *
     * @param id          the unique identifier of the status to update
     * @param name        the new name to assign to the status
     * @param description the new description to assign to the status
     */
    void editPaymentStatus(Long id, String name, String description);
}