package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing the status of a payment transaction.
 * <p>
 * This class encapsulates the details of a specific payment lifecycle state
 * (e.g., "Pending", "Approved", "Rejected"). It is used when managing the dictionary
 * of available payment statuses in the system via administrative forms.
 * </p>
 */
@Data
public class PaymentStatusDto {

    /**
     * The unique identifier of the payment status record.
     */
    private Long id;

    /**
     * The display name or label of the status (e.g., "Pending", "Completed").
     */
    private String name;

    /**
     * A brief description explaining what this status represents in the payment workflow.
     */
    private String description;
}