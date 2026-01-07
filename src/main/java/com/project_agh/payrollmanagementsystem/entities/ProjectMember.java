package com.project_agh.payrollmanagementsystem.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A helper model representing a member of a project team.
 * <p>
 * This class encapsulates the association between a {@link User} and their specific
 * role within a project context. It is typically used to populate the transient
 * members list in the {@link Project} entity, allowing the application to display
 * not just who is on the project, but what their specific function is.
 * </p>
 * <p>
 * Note: This is not a persistent entity itself, but rather a projection or wrapper used
 * by the business logic layer.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {

    /**
     * The user (employee) assigned to the project.
     */
    private User user;

    /**
     * The specific role or title the user holds within this project
     * (e.g., "Team Lead", "Developer", "Consultant").
     */
    private String projectRole;
}