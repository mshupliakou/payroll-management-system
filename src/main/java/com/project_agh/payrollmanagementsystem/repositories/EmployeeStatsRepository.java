package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.EmployeeStats;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for retrieving aggregated statistical data regarding employee work hours.
 * <p>
 * This interface defines operations to fetch pre-calculated metrics such as total hours worked
 * and weekly averages, typically sourced from a database view or aggregation query.
 * </p>
 */
@Repository
public interface EmployeeStatsRepository {

    /**
     * Retrieves the total accumulated work hours for a specific employee.
     *
     * @param id the unique identifier of the employee
     * @return the sum of all hours worked as a {@code Double}
     */
    Double sumAllById(Long id);

    /**
     * Retrieves the average number of hours worked per week for a specific employee.
     *
     * @param id the unique identifier of the employee
     * @return the average weekly hours as a {@code Double}
     */
    Double weekAverageById(Long id);

    /**
     * Retrieves the full statistical profile for a specific employee.
     * <p>
     * This includes total hours, the number of weeks worked, and the weekly average,
     * wrapped in an {@link EmployeeStats} object.
     * </p>
     *
     * @param id the unique identifier of the employee
     * @return an {@link Optional} containing the {@link EmployeeStats} if found, or empty otherwise
     */
    Optional<EmployeeStats> findById(Long id);
}