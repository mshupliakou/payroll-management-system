package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.Department;
import com.project_agh.payrollmanagementsystem.repositories.DepartmentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of the {@link DepartmentRepository} interface.
 * <p>
 * This repository manages the persistence of {@link Department} entities using raw SQL queries
 * executed via Spring's {@link JdbcTemplate}. It handles creating, retrieving, updating,
 * and deleting department records in the underlying 'dzial' table.
 * </p>
 */
@Repository
public class JdbcDepartmentRepository implements DepartmentRepository {

    private static final String FIND_ALL_FULL_SQL =
            "SELECT id_dzial, nazwa, opis FROM dzial";

    private static final String CREATE_NEW_DEPARTMENT =
            "INSERT INTO dzial (nazwa, opis) VALUES (?, ?)";

    private static final String DELETE_DEPARTMENT =
            "DELETE FROM dzial \n" +
                    "WHERE id_dzial = ?\n";

    private static final String EDIT_DEPARTMENT =
            "UPDATE dzial SET " +
                    "nazwa = ?, " +
                    "opis = ? " +
                    "WHERE id_dzial = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new {@link JdbcDepartmentRepository} with the given {@link JdbcTemplate}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used for database operations
     */
    public JdbcDepartmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Maps a row from the 'dzial' table to a {@link Department} entity.
     */
    private final RowMapper<Department> departmentRowMapper = (rs, rowNum) -> {
        Department department = new Department();
        department.setId(rs.getLong("id_dzial"));
        department.setName(rs.getString("nazwa"));
        department.setDescription(rs.getString("opis"));
        return department;
    };

    /**
     * Retrieves all departments from the database.
     *
     * @return a {@link List} of all {@link Department} entities currently persisted
     */
    @Override
    public List<Department> findAll() {
        return jdbcTemplate.query(FIND_ALL_FULL_SQL, departmentRowMapper);
    }

    /**
     * Creates a new department in the database with the specified name and description.
     *
     * @param department_name the name of the new department
     * @param department_desc a brief description of the new department
     */
    @Override
    public void createDepartment(String department_name, String department_desc) {
        int rowsAffected = jdbcTemplate.update(CREATE_NEW_DEPARTMENT, department_name, department_desc);

        if (rowsAffected != 1) {
            // Optional: handle the error if the row was not inserted
        }
    }

    /**
     * Deletes a department from the database.
     * <p>
     * Removes the record identified by the provided ID from the 'dzial' table.
     * </p>
     *
     * @param id the unique identifier of the department to delete
     */
    @Override
    public void deleteDepartment(Long id) {
        int rowsAffected = jdbcTemplate.update(
                DELETE_DEPARTMENT,
                id
        );

        if (rowsAffected != 1) {
            // Optional: Handle case where delete failed or ID didn't exist
        }
    }

    /**
     * Updates the details of an existing department.
     * <p>
     * Modifies the name and description of the department record identified by the given ID.
     * </p>
     *
     * @param id          the unique identifier of the department to update
     * @param name        the new name to assign to the department
     * @param description the new description to assign to the department
     */
    @Override
    public void editDepartment(Long id, String name, String description) {
        int rowsAffected = jdbcTemplate.update(
                EDIT_DEPARTMENT,
                name,
                description,
                id
        );

        if (rowsAffected != 1) {
            // Optional: Handle case where update failed or ID didn't exist
        }
    }
}