package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.WorkType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing work type definitions.
 * <p>
 * This interface defines operations to maintain the dictionary of work categories
 * (e.g., "Remote", "Office", "Business Trip"). These types are referenced when
 * employees log their work hours.
 * </p>
 */
@Repository
public interface WorkTypeRepository {

    /**
     * Retrieves all work types available in the system.
     *
     * @return a {@link List} containing all {@link WorkType} entities
     */
    List<WorkType> findAll();

    /**
     * Creates and persists a new work type definition.
     *
     * @param name        the unique name of the work type (e.g., "Remote")
     * @param description a brief description of the work type rules or context
     */
    void createWorkType(String name, String description);

    /**
     * Deletes a specific work type from the system.
     * <p>
     * <b>Note:</b> Deletion may be restricted if there are existing work logs referencing this type.
     * </p>
     *
     * @param id the unique identifier of the work type to remove
     */
    void deleteWorkType(Long id);

    /**
     * Updates the details of an existing work type.
     *
     * @param id          the unique identifier of the work type to update
     * @param name        the new name to assign to the work type
     * @param description the new description to assign to the work type
     */
    void editWorkType(Long id, String name, String description);
}