package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a specific category or type of payment.
 * <p>
 * This class encapsulates the definition of a payment type (e.g., "Regular Salary",
 * "Overtime", "Bonus"). It is used for transferring this configuration data between
 * the client-side forms and the server-side logic.
 * </p>
 */
@Data
public class PaymentTypeDto {

    /**
     * The unique identifier of the payment type record.
     */
    private Long id;

    /**
     * The display name of the payment type (e.g., "Bonus", "Regular Salary").
     */
    private String name;

    /**
     * A brief description explaining the purpose or rules associated with this payment type.
     */
    private String description;
}