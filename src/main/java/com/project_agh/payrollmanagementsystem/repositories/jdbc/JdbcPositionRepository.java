package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.Position;
import com.project_agh.payrollmanagementsystem.repositories.PositionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of the {@link PositionRepository} interface.
 * <p>
 * This repository manages the persistence of {@link Position} entities (job titles/roles)
 * using raw SQL queries executed via Spring's {@link JdbcTemplate}. It handles creating,
 * retrieving, updating, and deleting records in the {@code stanowisko} table.
 * </p>
 */
@Repository
public class JdbcPositionRepository implements PositionRepository {

    private static final String FIND_ALL_FULL_SQL =
            "SELECT id_stanowisko, nazwa, opis FROM stanowisko";

    private static final String CREATE_NEW_POSITION =
            "INSERT INTO stanowisko (nazwa, opis) VALUES (?, ?)";

    private static final String DELETE_POSITION =
            "DELETE FROM stanowisko \n" +
                    "WHERE id_stanowisko = ?\n";

    private static final String EDIT_POSITION =
            "UPDATE stanowisko SET " +
                    "nazwa = ?, " +
                    "opis = ? " +
                    "WHERE id_stanowisko = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new {@link JdbcPositionRepository} with the given {@link JdbcTemplate}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used for database operations
     */
    public JdbcPositionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Maps database rows from the 'stanowisko' table to {@link Position} objects.
     */
    private final RowMapper<Position> positionRowMapper = (rs, rowNum) -> {
        Position position = new Position();
        position.setId(rs.getLong("id_stanowisko"));
        position.setName(rs.getString("nazwa"));
        position.setDescription(rs.getString("opis"));
        return position;
    };

    /**
     * Retrieves all job positions from the database.
     *
     * @return a {@link List} of all {@link Position} entities currently persisted
     */
    @Override
    public List<Position> findAll() {
        return jdbcTemplate.query(FIND_ALL_FULL_SQL, positionRowMapper);
    }

    /**
     * Creates a new job position in the database.
     *
     * @param position_name the title of the new position
     * @param position_desc a brief description of the responsibilities for this position
     */
    @Override
    public void createPosition(String position_name, String position_desc) {
        int rowsAffected = jdbcTemplate.update(CREATE_NEW_POSITION, position_name, position_desc);

        if (rowsAffected != 1) {
            // Optional: handle the error if the row was not inserted
        }
    }

    /**
     * Updates the details of an existing job position.
     * <p>
     * Uses {@code jdbcTemplate.update()} to modify the name and description
     * for the record identified by the given ID.
     * </p>
     *
     * @param id          the unique identifier of the position to update
     * @param name        the new title to assign to the position
     * @param description the new description to assign to the position
     */
    @Override
    public void editPosition(Long id, String name, String description) {
        int rowsAffected = jdbcTemplate.update(
                EDIT_POSITION,
                name,
                description,
                id
        );

        // rowsAffected contains 1 if the update was successful
        if (rowsAffected != 1) {
            // Optional: Handle case where update failed
        }
    }

    /**
     * Deletes a job position from the database.
     *
     * @param id the unique identifier of the position to delete
     */
    @Override
    public void deletePosition(Long id) {
        int rowsAffected = jdbcTemplate.update(
                DELETE_POSITION,
                id
        );

        if (rowsAffected != 1) {
            // Optional: Handle case where delete failed
        }
    }
}