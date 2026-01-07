package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.WorkType;
import com.project_agh.payrollmanagementsystem.repositories.WorkTypeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of the {@link WorkTypeRepository}.
 * <p>
 * This repository manages the CRUD operations for {@link WorkType} entities,
 * allowing administrators to define different categories of work (e.g., "Remote",
 * "Office", "Business Trip") directly in the {@code typ_pracy} table.
 * </p>
 */
@Repository
public class JdbcWorkTypeRepository implements WorkTypeRepository {

    private static final String FIND_ALL_FULL_SQL =
            "SELECT * FROM typ_pracy";

    private static final String CREATE_NEW_WORK_TYPE =
            "INSERT INTO typ_pracy (nazwa, opis) VALUES (?, ?)";

    private static final String DELETE_WORK_TYPE =
            "DELETE FROM typ_pracy \n" +
                    "WHERE id_typ_pracy = ?\n";

    private static final String EDIT_WORK_TYPE =
            "UPDATE typ_pracy SET " +
                    "nazwa = ?, " +
                    "opis = ?" +
                    " WHERE id_typ_pracy = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new {@code JdbcWorkTypeRepository}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used for executing SQL queries
     */
    public JdbcWorkTypeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Maps database rows from the 'typ_pracy' table to {@link WorkType} objects.
     */
    private final RowMapper<WorkType> workTypeRowMapper = (rs, rowNum) -> {
        WorkType workType = new WorkType();
        workType.setId(rs.getLong("id_typ_pracy"));
        workType.setName(rs.getString("nazwa"));
        workType.setDescription(rs.getString("opis"));
        return workType;
    };

    /**
     * Retrieves all defined work types from the database.
     *
     * @return a {@link List} of all {@link WorkType} entities
     */
    @Override
    public List<WorkType> findAll() {
        return jdbcTemplate.query(FIND_ALL_FULL_SQL, workTypeRowMapper);
    }

    /**
     * Creates and persists a new work type definition.
     *
     * @param name        the unique name of the work type (e.g., "Remote")
     * @param description a brief description of the rules for this work type
     */
    @Override
    public void createWorkType(String name, String description) {
        int rowsAffected = jdbcTemplate.update(CREATE_NEW_WORK_TYPE, name, description);

        if (rowsAffected != 1) {
            // Optional: handle the error if the row was not inserted
        }
    }

    /**
     * Deletes a specific work type from the database.
     *
     * @param id the unique identifier of the work type to remove
     */
    @Override
    public void deleteWorkType(Long id) {
        int rowsAffected = jdbcTemplate.update(
                DELETE_WORK_TYPE,
                id
        );

        if (rowsAffected != 1) {
            // Optional: Handle case where delete failed
        }
    }

    /**
     * Updates the details of an existing work type.
     *
     * @param id          the unique identifier of the work type to update
     * @param name        the new name to assign to the work type
     * @param description the new description to assign to the work type
     */
    @Override
    public void editWorkType(Long id, String name, String description) {
        int rowsAffected = jdbcTemplate.update(
                EDIT_WORK_TYPE,
                name,
                description,
                id
        );

        if (rowsAffected != 1) {
            // Optional: Handle case where update failed
        }
    }
}