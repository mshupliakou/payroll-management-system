package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.EmployeeStats;
import com.project_agh.payrollmanagementsystem.repositories.EmployeeStatsRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JDBC-based implementation of the {@link EmployeeStatsRepository}.
 * <p>
 * This repository retrieves aggregated statistical data regarding employee work hours.
 * It queries the database view {@code widok_statystyki_pracownika}, which pre-calculates
 * totals and averages, ensuring efficient data retrieval for dashboards and reports.
 * </p>
 */
@Repository
public class JdbcEmployeeStatsRepository implements EmployeeStatsRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM widok_statystyki_pracownika WHERE id_pracownik = ?";

    /**
     * Constructs a new {@code JdbcEmployeeStatsRepository}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used to execute SQL queries
     */
    public JdbcEmployeeStatsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Maps rows from the {@code widok_statystyki_pracownika} view to {@link EmployeeStats} entities.
     */
    private final RowMapper<EmployeeStats> statsRowMapper = (rs, rowNum) -> {
        EmployeeStats stats = new EmployeeStats();
        stats.setId(rs.getLong("id_pracownik"));
        stats.setTotalHours(rs.getDouble("suma_calkowita"));
        stats.setWeeksCount(rs.getInt("liczba_tygodni"));
        stats.setAverageWeeklyHours(rs.getDouble("srednia_tygodniowa"));
        return stats;
    };

    /**
     * Retrieves the full statistical profile for a specific employee.
     *
     * @param id the unique identifier of the employee
     * @return an {@link Optional} containing the {@link EmployeeStats} if found, or empty if not found or an error occurs
     */
    @Override
    public Optional<EmployeeStats> findById(Long id) {
        try {
            List<EmployeeStats> results = jdbcTemplate.query(
                    FIND_BY_ID_SQL,
                    statsRowMapper,
                    id
            );

            return results.stream().findFirst();

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieves only the total accumulated work hours for a specific employee.
     * <p>
     * Returns 0.0 if the employee has no recorded hours or does not exist in the statistics view.
     * </p>
     *
     * @param id the unique identifier of the employee
     * @return the total hours worked as a {@code Double}
     */
    public Double sumAllById(Long id) {
        String sql = "SELECT suma_calkowita FROM widok_statystyki_pracownika WHERE id_pracownik = ?";
        try {
            // Returning Double rather than int to accommodate fractional hours
            return jdbcTemplate.queryForObject(sql, Double.class, id);
        } catch (EmptyResultDataAccessException e) {
            return 0.0;
        }
    }

    /**
     * Retrieves only the average weekly work hours for a specific employee.
     * <p>
     * Returns 0.0 if the employee has no recorded hours or does not exist in the statistics view.
     * </p>
     *
     * @param id the unique identifier of the employee
     * @return the average weekly hours as a {@code Double}
     */
    public Double weekAverageById(Long id) {
        String sql = "SELECT srednia_tygodniowa FROM widok_statystyki_pracownika WHERE id_pracownik = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, id);
        } catch (EmptyResultDataAccessException e) {
            return 0.0;
        }
    }

}