package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a project within the organization.
 * <p>
 * This class captures the essential details of a project, including its identity,
 * descriptive metadata, and temporal scope (start and end dates). It is primarily
 * used for form binding during project creation and modification in the administrative panel.
 * </p>
 */
@Data
public class ProjectDto {

    /**
     * The unique identifier of the project.
     */
    private Long id;

    /**
     * The official name or title of the project.
     */
    private String name;

    /**
     * A brief description outlining the goals, scope, or details of the project.
     */
    private String description;

    /**
     * The designated start date of the project.
     * <p>
     * The {@code @DateTimeFormat} annotation ensures that the date string received
     * from the frontend (in "yyyy-MM-dd" format) is correctly parsed into a {@link LocalDate}.
     * </p>
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectBeginDate;

    /**
     * The projected or actual end date of the project.
     * <p>
     * Like the start date, this field requires the "yyyy-MM-dd" format for proper binding.
     * </p>
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEndDate;

}