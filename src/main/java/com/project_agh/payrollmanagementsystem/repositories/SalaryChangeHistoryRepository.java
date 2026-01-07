package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.SalaryChangeHistory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing salary adjustment records.
 * <p>
 * This interface defines operations to log changes in employee compensation (auditing)
 * and retrieve the historical data of these changes for reporting purposes.
 * </p>
 */
@Repository
public interface SalaryChangeHistoryRepository {

    /**
     * Records a salary change event for a specific employee.
     * <p>
     * This method is responsible for creating a new audit log entry containing the
     * previous salary, the new salary, and the effective date of the change.
     * </p>
     *
     * @param userId    the unique identifier of the employee (User)
     * @param oldSalary the salary amount prior to the adjustment
     * @param newSalary the new salary amount being applied
     * @param date      the date on which this change takes effect
     */
    void changeSalary(Long userId, BigDecimal oldSalary, BigDecimal newSalary, LocalDate date);

    /**
     * Retrieves the complete history of salary changes across the entire organization.
     *
     * @return a {@link List} of all {@link SalaryChangeHistory} entities, typically ordered by date
     */
    List<SalaryChangeHistory> findAll();
}