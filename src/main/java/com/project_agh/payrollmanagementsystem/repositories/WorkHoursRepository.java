package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.WorkHours;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Repository interface for managing work hour records.
 * <p>
 * This interface defines operations to log, retrieve, and modify work time entries for employees.
 * It supports linking work to specific projects, filtering records by date ranges for payroll processing,
 * and managing the approval workflow.
 * </p>
 */
@Repository
public interface WorkHoursRepository {

    /**
     * Creates a work hour entry linked to a specific project.
     *
     * @param currentUserId the unique identifier of the employee logging the work
     * @param date          the date the work was performed
     * @param workType      the identifier of the type of work (e.g., Remote, Office)
     * @param projectId     the identifier of the project the work is associated with
     * @param startTime     the time the work session began
     * @param endTime       the time the work session ended
     * @param comment       optional notes regarding the tasks performed
     */
    void createWorkHoursWithProject(Long currentUserId, LocalDate date, Long workType, Long projectId, LocalTime startTime, LocalTime endTime, String comment);

    /**
     * Creates a general work hour entry (not linked to any specific project).
     *
     * @param currentUserId the unique identifier of the employee logging the work
     * @param date          the date the work was performed
     * @param workType      the identifier of the type of work
     * @param startTime     the time the work session began
     * @param endTime       the time the work session ended
     * @param comment       optional notes regarding the tasks performed
     */
    void createWorkHours(Long currentUserId, LocalDate date, Long workType, LocalTime startTime, LocalTime endTime, String comment);

    /**
     * Retrieves the complete history of work hours for a specific employee.
     *
     * @param userId the unique identifier of the employee
     * @return a {@link List} of {@link WorkHours} entries for that user
     */
    List<WorkHours> findByUserId(Long userId);

    /**
     * Retrieves all work hour records in the system.
     * <p>
     * Typically used by administrators or for system-wide reporting.
     * </p>
     *
     * @return a {@link List} of all {@link WorkHours} entries
     */
    List<WorkHours> findAll();

    /**
     * Deletes a specific work hour entry.
     *
     * @param id the unique identifier of the work hour record
     */
    void deleteWorkHours(Long id);

    /**
     * Updates an existing work hour entry.
     * <p>
     * <b>Note:</b> Editing a record typically resets its approval status to "unapproved".
     * </p>
     *
     * @param id            the unique identifier of the record to update
     * @param currentUserId the ID of the user performing the edit (often used for validation)
     * @param date          the new date
     * @param workType      the new work type ID
     * @param startTime     the new start time
     * @param endTime       the new end time
     * @param comment       the new comment
     */
    void editWorkHours(Long id, Long currentUserId, LocalDate date, Long workType, LocalTime startTime, LocalTime endTime, String comment);

    /**
     * Retrieves work hours for a specific user within a given date range.
     * <p>
     * Used for generating weekly or monthly timesheets for a single employee.
     * </p>
     *
     * @param id          the unique identifier of the user
     * @param startOfWeek the start date of the range (inclusive)
     * @param endOfWeek   the end date of the range (inclusive)
     * @return a {@link List} of matching {@link WorkHours} entries
     */
    List<WorkHours> findByUserIdAndDateRange(Long id, LocalDate startOfWeek, LocalDate endOfWeek);

    /**
     * Retrieves work hours for all users within a given date range.
     * <p>
     * Used by accountants to calculate payroll for a specific pay period.
     * </p>
     *
     * @param startOfWeek the start date of the range (inclusive)
     * @param endOfWeek   the end date of the range (inclusive)
     * @return a {@link List} of matching {@link WorkHours} entries
     */
    List<WorkHours> findByDateRange(LocalDate startOfWeek, LocalDate endOfWeek);

    /**
     * Marks a specific work hour record as approved.
     * <p>
     * Approved records are typically locked from further editing and are ready for payroll processing.
     * </p>
     *
     * @param id the unique identifier of the work hour record to approve
     */
    void approveWorkHours(Long id);
}