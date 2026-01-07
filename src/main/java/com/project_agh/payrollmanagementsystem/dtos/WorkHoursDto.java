package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) responsible for capturing and transferring work hour entries.
 * <p>
 * This class encapsulates the details of a specific work session, including the timeframe
 * (date, start/end time), the associated project, the type of work performed, and any
 * optional comments. It is primarily used in forms for logging and editing time records.
 * </p>
 */
@Data
public class WorkHoursDto {

    /**
     * The unique identifier of the work hour record.
     * <p>
     * This field is typically null when creating a new entry and populated when editing an existing one.
     * </p>
     */
    private Long id;

    /**
     * The specific date on which the work was performed.
     * <p>
     * The {@code @DateTimeFormat} annotation ensures that the date string received
     * from the frontend (in "yyyy-MM-dd" format) is correctly parsed into a {@link LocalDate}.
     * </p>
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * The time of day when the work session began.
     */
    private LocalTime startTime;

    /**
     * The time of day when the work session ended.
     */
    private LocalTime endTime;

    /**
     * The unique identifier of the user (employee) who performed the work.
     */
    private Long userId;

    /**
     * The unique identifier of the project associated with this work session.
     * <p>
     * This field may be optional depending on whether the work is general or project-specific.
     * </p>
     */
    private Long projectId;

    /**
     * The unique identifier of the work type category (e.g., "Remote", "Office", "Business Trip").
     */
    private Long workTypeId;

    /**
     * An optional text note or description regarding the tasks performed during this session.
     */
    private String comment;
}