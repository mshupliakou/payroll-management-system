package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.Project;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Project} entities.
 * <p>
 * This interface defines the contract for project lifecycle management, including creating projects,
 * updating details, and managing the association of employees (users) to specific projects.
 * </p>
 */
@Repository
public interface ProjectRepository {

    /**
     * Retrieves a specific project by its unique identifier.
     *
     * @param id the unique identifier of the project
     * @return an {@link Optional} containing the {@link Project} if found, or empty otherwise
     */
    Optional<Project> findById(Long id);

    /**
     * Retrieves all projects currently registered in the system.
     *
     * @return a {@link List} of all {@link Project} entities
     */
    List<Project> findAll();

    /**
     * Removes an employee from a specific project team.
     *
     * @param projectId the unique identifier of the project
     * @param userId    the unique identifier of the user (employee) to remove
     */
    void removeUserFromProject(Long projectId, Long userId);

    /**
     * Assigns an employee to a project with a specific role.
     * <p>
     * If the user is already assigned to the project, this operation typically updates their role
     * rather than creating a duplicate entry.
     * </p>
     *
     * @param project_id  the unique identifier of the project
     * @param user_id     the unique identifier of the user (employee)
     * @param projectRole the role the user will fulfill in this project (e.g., "Developer", "Lead")
     * @param userAdded   the date the user was assigned to the project
     */
    void addUserToProject(Long project_id, Long user_id, String projectRole, LocalDate userAdded);

    /**
     * Creates and persists a new project.
     *
     * @param project_name       the name of the project
     * @param project_desc       a description of the project's scope and goals
     * @param project_start_date the date the project officially begins
     * @param project_end_date   the expected or actual completion date (can be null if ongoing)
     */
    void createProject(String project_name, String project_desc, LocalDate project_start_date, LocalDate project_end_date);

    /**
     * Deletes a project from the system.
     *
     * @param id the unique identifier of the project to remove
     */
    void deleteProject(Long id);

    /**
     * Updates the details of an existing project.
     *
     * @param id                 the unique identifier of the project to update
     * @param name               the new name of the project
     * @param description        the new description of the project
     * @param project_start_date the new start date
     * @param project_end_date   the new end date
     */
    void editProject(Long id, String name, String description, LocalDate project_start_date, LocalDate project_end_date);
}