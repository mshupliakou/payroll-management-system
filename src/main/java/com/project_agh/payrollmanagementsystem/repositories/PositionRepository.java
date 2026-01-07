package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.Position;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing operations on {@link Position} entities.
 * <p>
 * This interface defines the contract for managing job positions (job titles/roles)
 * within the payroll management system, including retrieval, creation, modification,
 * and deletion of position records.
 * </p>
 */
@Repository
public interface PositionRepository {

    /**
     * Retrieves all job positions available in the system.
     *
     * @return a {@link List} containing all {@link Position} entities
     */
    List<Position> findAll();

    /**
     * Deletes a specific job position from the system.
     * <p>
     * <b>Note:</b> Deletion may be restricted if there are active employees currently
     * assigned to this position.
     * </p>
     *
     * @param id the unique identifier of the position to remove
     */
    void deletePosition(Long id);

    /**
     * Creates and persists a new job position.
     *
     * @param position_name the title of the new position (e.g., "Senior Developer")
     * @param position_desc a brief description of the responsibilities associated with this position
     */
    void createPosition(String position_name, String position_desc);

    /**
     * Updates the details of an existing job position.
     *
     * @param id          the unique identifier of the position to update
     * @param name        the new title to assign to the position
     * @param description the new description to assign to the position
     */
    void editPosition(Long id, String name, String description);
}