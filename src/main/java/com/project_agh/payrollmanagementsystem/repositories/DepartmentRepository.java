package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing persistence operations on {@link Department} entities.
 * <p>
 * This interface defines the contract for managing organizational departments, including
 * retrieval, creation, modification, and deletion of department records.
 * Implementations (e.g., JDBC-based) handle the underlying database interactions.
 * </p>
 */
@Repository
public interface DepartmentRepository {

    /**
     * Retrieves a list of all departments currently existing in the system.
     *
     * @return a {@link List} containing all {@link Department} entities
     */
    List<Department> findAll();

    /**
     * Creates and persists a new department.
     *
     * @param department_name the unique name of the new department (e.g., "Human Resources")
     * @param department_desc a brief description of the department's function or scope
     */
    void createDepartment(String department_name, String department_desc);

    /**
     * Deletes a specific department from the system.
     * <p>
     * <b>Note:</b> Deletion may fail or be restricted if there are existing employees
     * or other entities currently linked to this department.
     * </p>
     *
     * @param id the unique identifier of the department to remove
     */
    void deleteDepartment(Long id);

    /**
     * Updates the details of an existing department.
     *
     * @param id          the unique identifier of the department to edit
     * @param name        the new name to assign to the department
     * @param description the new description to assign to the department
     */
    void editDepartment(Long id, String name, String description);
}