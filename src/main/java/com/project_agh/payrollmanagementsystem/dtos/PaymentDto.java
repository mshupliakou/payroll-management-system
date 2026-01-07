package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a payment transaction within the system.
 * <p>
 * This class encapsulates the details of a financial transfer to an employee,
 * including the amount, date, recipient ID, and a description of the payment.
 * It is used to transfer payment data between the service layer and the view.
 * </p>
 */
@Data
public class PaymentDto {

    /**
     * The unique identifier of the payment record in the database.
     */
    private Long id;

    /**
     * The unique identifier of the user (employee) who is the recipient of this payment.
     */
    private Long userId;

    /**
     * The date on which the payment was processed or scheduled.
     */
    private LocalDate date;

    /**
     * The monetary value of the payment.
     * <p>
     * {@link BigDecimal} is used to ensure precision for financial calculations.
     * </p>
     */
    private BigDecimal amount;

    /**
     * A secondary or specific identifier associated with the payment details
     * (e.g., a reference to a specific payment type or external transaction ID).
     */
    private Long paymentId;

    /**
     * A textual description or note explaining the purpose of the payment
     * (e.g., "Salary for October", "Project Bonus").
     */
    private String description;
}