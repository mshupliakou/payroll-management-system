package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.SalaryChangeHistory;
import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.repositories.SalaryChangeHistoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * JDBC-based implementation of the {@link SalaryChangeHistoryRepository}.
 * <p>
 * This repository manages the persistence and retrieval of salary adjustment logs.
 * It utilizes SQL JOINS to fetch the history records along with the associated employee's
 * basic details in a single query, providing an efficient audit trail for the application.
 * </p>
 */
@Repository
public class JdbcSalaryChangeHistoryRepository implements SalaryChangeHistoryRepository {

    private static final String FIND_ALL_WITH_USERS_SQL =
            "SELECT h.*, " +
                    "       p.imie AS user_name, " +
                    "       p.nazwisko AS user_lastname, " +
                    "       p.email AS user_email " +
                    "FROM historia_zmian_wynagrodzen h " +
                    "JOIN pracownik p ON h.id_pracownik = p.id_pracownik " +
                    "ORDER BY h.data DESC";

    private static final String CHANGE_SALARY_SQL =
            "INSERT INTO historia_zmian_wynagrodzen (id_pracownik, stare_wynagr, nowe_wynagr, data, opis) VALUES (?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new {@code JdbcSalaryChangeHistoryRepository}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used for database operations
     */
    public JdbcSalaryChangeHistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Maps database rows to {@link SalaryChangeHistory} entities.
     * <p>
     * <b>Note:</b> This mapper manually constructs the {@link User} object using
     * aliased columns ('user_name', 'user_lastname', etc.) retrieved from the SQL JOIN.
     * This avoids the N+1 select problem.
     * </p>
     */
    private final RowMapper<SalaryChangeHistory> salaryChangeHistoryRowMapper = (rs, rowNum) -> {
        SalaryChangeHistory history = new SalaryChangeHistory();

        history.setId(rs.getLong("id_zmiany_wynagrodzenia"));
        history.setOldSalary(rs.getBigDecimal("stare_wynagr"));
        history.setNewSalary(rs.getBigDecimal("nowe_wynagr"));

        Date sqlDate = rs.getDate("data");
        if (sqlDate != null) {
            history.setDate(sqlDate.toLocalDate());
        }

        history.setDescription(rs.getString("opis"));

        // Map the joined User data
        User user = new User();
        user.setId(rs.getLong("id_pracownik"));
        user.setName(rs.getString("user_name"));
        user.setLastname(rs.getString("user_lastname"));
        user.setEmail(rs.getString("user_email"));

        history.setUser(user);

        return history;
    };

    /**
     * Records a new salary change event in the database.
     * <p>
     * Inserts a record into the {@code historia_zmian_wynagrodzen} table tracking the old and new
     * salary amounts.
     * </p>
     *
     * @param userId    the unique identifier of the employee
     * @param oldSalary the salary amount before the change
     * @param newSalary the new salary amount
     * @param date      the effective date of the change
     */
    @Override
    public void changeSalary(Long userId, BigDecimal oldSalary, BigDecimal newSalary, LocalDate date) {
        jdbcTemplate.update(
                CHANGE_SALARY_SQL,
                userId,
                oldSalary,
                newSalary,
                date,
                null // Description is currently optional/null by default in this specific method
        );
    }

    /**
     * Retrieves the complete history of salary changes across all employees.
     * <p>
     * The results are ordered by date in descending order (newest changes first).
     * </p>
     *
     * @return a {@link List} of all {@link SalaryChangeHistory} entities
     */
    @Override
    public List<SalaryChangeHistory> findAll() {
        return jdbcTemplate.query(FIND_ALL_WITH_USERS_SQL, salaryChangeHistoryRowMapper);
    }
}