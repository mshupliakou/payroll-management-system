package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing aggregated work statistics for a specific employee.
 * <p>
 * This class is used to transfer calculated statistical data, such as total hours worked,
 * the number of active weeks, and weekly averages, from the database view or service layer
 * to the frontend dashboard.
 * </p>
 */
@Data
public class EmployeeStatsDto {

    /**
     * The unique identifier of the employee to whom these statistics belong.
     */
    Long employeeId;

    /**
     * The total sum of hours worked by the employee across all recorded history.
     * <p>
     * Note: Depending on the implementation, this might represent raw minutes or rounded hours.
     * </p>
     */
    int sumAll;

    /**
     * The count of distinct weeks in which the employee has recorded work activity.
     */
    int weekAmount;

    /**
     * The calculated average number of hours worked per week.
     * <p>
     * Typically derived by dividing {@code sumAll} by {@code weekAmount}.
     * </p>
     */
    int weekHoursAverage;

}