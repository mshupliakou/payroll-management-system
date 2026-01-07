package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.PaymentType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing payment type definitions.
 * <p>
 * This interface defines operations to maintain the dictionary of payment categories
 * (e.g., "Regular Salary", "Bonus", "Overtime", "Severance"). These types are used
 * to classify financial transactions within the payroll system.
 * </p>
 */
@Repository
public interface PaymentTypeRepository {

    /**
     * Retrieves all available payment types defined in the system.
     *
     * @return a {@link List} of all {@link PaymentType} entities
     */
    List<PaymentType> findAll();

    /**
     * Creates and persists a new payment type definition.
     *
     * @param name        the unique name of the payment type (e.g., "Holiday Bonus")
     * @param description a brief description of the purpose or rules for this payment type
     */
    void createPaymentType(String name, String description);

    /**
     * Deletes a payment type definition from the system.
     *
     * @param id the unique identifier of the payment type to remove
     */
    void deletePaymentType(Long id);

    /**
     * Updates the details of an existing payment type.
     *
     * @param id          the unique identifier of the payment type to update
     * @param name        the new name to assign to the payment type
     * @param description the new description to assign to the payment type
     */
    void editPaymentType(Long id, String name, String description);
}