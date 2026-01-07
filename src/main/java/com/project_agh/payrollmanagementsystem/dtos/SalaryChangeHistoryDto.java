package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a record of a salary adjustment for an employee.
 * <p>
 * This class encapsulates the details of a specific change in an employee's compensation,
 * including the previous amount, the new amount, the date of the change, and the reason.
 * It is used to display the salary history log in the user or accountant dashboard.
 * </p>
 */
@Data
public class SalaryChangeHistoryDto {

    /**
     * The unique identifier of the salary change history record.
     */
    private Long id;

    /**
     * The unique identifier of the user (employee) subject to the salary change.
     */
    private Long userId;

    /**
     * The monetary value of the salary prior to the adjustment.
     */
    private BigDecimal oldSalary;

    /**
     * The new monetary value of the salary after the adjustment.
     */
    private BigDecimal newSalary;

    /**
     * The date on which the salary change became effective or was recorded.
     */
    private LocalDate date;

    /**
     * A textual description or reason for the salary adjustment (e.g., "Annual Review", "Promotion").
     */
    private String description;
}