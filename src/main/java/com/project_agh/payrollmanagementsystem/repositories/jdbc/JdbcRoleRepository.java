package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.Role;
import com.project_agh.payrollmanagementsystem.repositories.RoleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of the {@link RoleRepository}.
 * <p>
 * This repository manages the lifecycle of {@link Role} entities, which define
 * the security authorities granted to users (e.g., "ROLE_ADMIN", "ROLE_USER").
 * It handles standard CRUD operations directly against the {@code rola} table.
 * </p>
 */
@Repository
public class JdbcRoleRepository implements RoleRepository {

    private static final String FIND_ALL_FULL_SQL =
            "SELECT id_rola, nazwa FROM rola";

    private static final String CREATE_NEW_ROLE =
            "INSERT INTO rola (nazwa) VALUES (?)";

    private static final String EDIT_ROLE =
            "UPDATE rola SET " +
                    "nazwa = ? " +
                    "WHERE id_rola = ?";

    private static final String DELETE_ROLE =
            "DELETE FROM rola " +
                    "WHERE id_rola = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new {@code JdbcRoleRepository}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used for executing SQL queries
     */
    public JdbcRoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Maps database rows from the 'rola' table to {@link Role} objects.
     */
    private final RowMapper<Role> roleRowMapper = (rs, rowNum) -> {
        Role role = new Role();
        role.setId(rs.getLong("id_rola"));
        role.setName(rs.getString("nazwa"));
        return role;
    };

    /**
     * Retrieves all defined security roles from the database.
     *
     * @return a {@link List} of all {@link Role} entities
     */
    @Override
    public List<Role> findAll() {
        return jdbcTemplate.query(FIND_ALL_FULL_SQL, roleRowMapper);
    }

    /**
     * Creates and persists a new security role.
     *
     * @param role_name the name of the new role (e.g., "MANAGER")
     */
    @Override
    public void createRole(String role_name) {
        int rowsAffected = jdbcTemplate.update(CREATE_NEW_ROLE, role_name);
    }

    /**
     * Deletes a specific security role from the database.
     *
     * @param id the unique identifier of the role to remove
     */
    @Override
    public void deleteRole(Long id) {
        int rowsAffected = jdbcTemplate.update(
                DELETE_ROLE,
                id
        );

        if (rowsAffected != 1) {
            // Optional: Handle case where delete failed
        }
    }

    /**
     * Updates the name of an existing security role.
     *
     * @param id   the unique identifier of the role to update
     * @param name the new name to assign to the role
     */
    @Override
    public void editRole(Long id, String name) {
        int rowsAffected = jdbcTemplate.update(
                EDIT_ROLE,
                name,
                id
        );
        // Optional: Check rowsAffected to ensure update occurred
    }
}