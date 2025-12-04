package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.Department;
import com.project_agh.payrollmanagementsystem.repositories.DepartmentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC implementation of {@link DepartmentRepository}.
 * <p>
 * Provides methods to retrieve and create {@link Department} entities
 * using Spring's {@link JdbcTemplate}.
 * </p>
 */
@Repository
public class JdbcDepartmentRepository implements DepartmentRepository {

    private static final String FIND_ALL_FULL_SQL =
            "SELECT id_dzial, nazwa, opis FROM dzial";

    private static final String CREATE_NEW_DEPARTMENT =
            "INSERT INTO dzial (nazwa, opis) VALUES (?, ?)";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new {@link JdbcDepartmentRepository} with the given {@link JdbcTemplate}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used for database operations
     */
    public JdbcDepartmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
     * @return a {@link List} of all {@link Department} entities
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
}
