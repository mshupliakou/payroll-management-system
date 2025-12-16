package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.Role;

import java.util.List;

/**
 * Repository interface for performing operations on {@link Role} entities.
 * <p>
 * Provides methods to retrieve information about user roles
 * within the payroll management system.
 * </p>
 */
public interface RoleRepository {

    /**
     * Retrieves all roles available in the system.
     *
     * @return a {@link List} containing all {@link Role} entities
     */
    List<Role> findAll();

    void createRole(String role_name );

    void deleteRole(Long id);

    void editRole(Long id, String name);
}
