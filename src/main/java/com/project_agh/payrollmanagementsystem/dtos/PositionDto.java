package com.project_agh.payrollmanagementsystem.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a job position within the organization.
 * <p>
 * This class encapsulates the data required to define a specific job role, including
 * its title and a description of responsibilities. It is used primarily in administrative
 * forms for creating or editing organizational structure.
 * </p>
 */
@Data
public class PositionDto {

    /**
     * The unique identifier of the position record.
     */
    Long id;

    /**
     * The title or name of the job position (e.g., "Senior Developer", "HR Manager").
     */
    private String position_name;

    /**
     * A detailed description of the responsibilities, requirements, or scope associated
     * with this position.
     */
    private String position_desc;
}