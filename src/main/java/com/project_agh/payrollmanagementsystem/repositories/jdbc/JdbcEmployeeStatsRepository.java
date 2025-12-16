package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.EmployeeStats;
import com.project_agh.payrollmanagementsystem.repositories.EmployeeStatsRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcEmployeeStatsRepository implements EmployeeStatsRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_BY_ID_SQL =
            "SELECT * FROM widok_statystyki_pracownika WHERE id_pracownik = ?";

    public JdbcEmployeeStatsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<EmployeeStats> statsRowMapper = (rs, rowNum) -> {
        EmployeeStats stats = new EmployeeStats();
        stats.setId(rs.getLong("id_pracownik"));

        stats.setTotalHours(rs.getDouble("suma_calkowita"));

        stats.setWeeksCount(rs.getInt("liczba_tygodni"));

        stats.setAverageWeeklyHours(rs.getDouble("srednia_tygodniowa"));

        return stats;
    };

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

    public Double sumAllById(Long id) {
        String sql = "SELECT suma_calkowita FROM widok_statystyki_pracownika WHERE id_pracownik = ?";
        try {
            // Zwracamy Double, nie int, bo godziny mogą być ułamkowe
            return jdbcTemplate.queryForObject(sql, Double.class, id);
        } catch (EmptyResultDataAccessException e) {
            return 0.0;
        }
    }

    public Double weekAverageById(Long id) {
        String sql = "SELECT srednia_tygodniowa FROM widok_statystyki_pracownika WHERE id_pracownik = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, id);
        } catch (EmptyResultDataAccessException e) {
            return 0.0;
        }
    }

}