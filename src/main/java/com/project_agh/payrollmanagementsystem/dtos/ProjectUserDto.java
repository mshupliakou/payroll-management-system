package com.project_agh.payrollmanagementsystem.dtos;

import com.project_agh.payrollmanagementsystem.entities.User;
import lombok.Data;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the assignment of a user to a specific project.
 * <p>
 * This class captures the details of the relationship between a user and a project,
 * including the user's specific role within that project and the date the assignment begins.
 * It is used for managing project teams and resource allocation.
 * </p>
 */
@Data
public class ProjectUserDto {

    /**
     * The unique identifier of the user being assigned to the project.
     */
    Long user_id;

    /**
     * The unique identifier of the project to which the user is being assigned.
     */
    Long project_id;

    /**
     * The specific role or title the user holds within the context of this project
     * (e.g., "Lead Developer", "QA Tester").
     */
    String project_role;

    /**
     * The date on which the user's assignment to the project officially begins.
     */
    LocalDate project_start_date;

    /**
     * The full {@link User} entity object associated with this assignment.
     * <p>
     * This field is typically used for display purposes (e.g., showing the user's name
     * or email in a list of project members) rather than for persistence logic.
     * </p>
     */
    private User user;
}