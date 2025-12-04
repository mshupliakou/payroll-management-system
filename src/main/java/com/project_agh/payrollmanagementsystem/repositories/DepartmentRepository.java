package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing operations on {@link Department} entities.
 * <p>
 * Provides methods to retrieve and create departments
 * within the payroll management system.
 * </p>
 */
@Repository
public interface DepartmentRepository {

    /**
     * Retrieves all departments available in the system.
     *
     * @return a {@link List} containing all {@link Department} entities
     */
    List<Department> findAll();

    /**
     * Creates a new department with the specified name and description.
     *
     * @param department_name the name of the new department
     * @param department_desc a brief description of the department
     */
    void createDepartment(String department_name, String department_desc);
}
