package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing operations on {@link Role} entities.
 * <p>
 * This interface defines the contract for managing security roles (authorities) within the
 * payroll management system. It provides methods to retrieve, create, update, and delete roles,
 * which are used to control user access levels (e.g., "ROLE_ADMIN", "ROLE_USER").
 * </p>
 */
@Repository
public interface RoleRepository {

    /**
     * Retrieves all security roles available in the system.
     *
     * @return a {@link List} containing all {@link Role} entities
     */
    List<Role> findAll();

    /**
     * Creates and persists a new security role.
     *
     * @param role_name the name of the new role (e.g., "MANAGER")
     */
    void createRole(String role_name);

    /**
     * Deletes a specific security role from the system.
     * <p>
     * <b>Note:</b> Deleting a role may affect users currently assigned to it, potentially
     * stripping them of their access rights.
     * </p>
     *
     * @param id the unique identifier of the role to remove
     */
    void deleteRole(Long id);

    /**
     * Updates the name of an existing security role.
     *
     * @param id   the unique identifier of the role to update
     * @param name the new name to assign to the role
     */
    void editRole(Long id, String name);
}