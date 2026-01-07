package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.PaymentStatus;
import com.project_agh.payrollmanagementsystem.repositories.PaymentStatusRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of the {@link PaymentStatusRepository}.
 * <p>
 * This repository manages the CRUD operations for {@link PaymentStatus} entities,
 * allowing the system administrators to define and modify the available states for payments
 * (e.g., "Pending", "Approved", "Paid") directly in the {@code status_wyplaty} table.
 * </p>
 */
@Repository
public class JdbcPaymentStatusRepository implements PaymentStatusRepository {

    private static final String FIND_ALL_FULL_SQL =
            "SELECT * FROM status_wyplaty";

    private static final String CREATE_NEW_PAYMENT_STATUS =
            "INSERT INTO status_wyplaty (nazwa, opis) VALUES (?, ?)";

    private static final String DELETE_PAYMENT_STATUS =
            "DELETE FROM status_wyplaty \n" +
                    "WHERE id_status_wyplaty = ?\n";

    private static final String EDIT_PAYMENT_STATUS =
            "UPDATE status_wyplaty SET " +
                    "nazwa = ?, " +
                    "opis = ? " +
                    "WHERE id_status_wyplaty = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Maps database rows from the 'status_wyplaty' table to {@link PaymentStatus} objects.
     */
    private final RowMapper<PaymentStatus> paymentStatusRowMapper = (rs, rowNum) -> {
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setId(rs.getLong("id_status_wyplaty"));
        paymentStatus.setName(rs.getString("nazwa"));
        paymentStatus.setDescription(rs.getString("opis"));
        return paymentStatus;
    };

    /**
     * Constructs a new {@code JdbcPaymentStatusRepository}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used for executing SQL queries
     */
    public JdbcPaymentStatusRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves all defined payment statuses from the database.
     *
     * @return a {@link List} of {@link PaymentStatus} entities
     */
    @Override
    public List<PaymentStatus> findAll() {
        return jdbcTemplate.query(FIND_ALL_FULL_SQL, paymentStatusRowMapper);
    }

    /**
     * Creates and persists a new payment status definition.
     *
     * @param name        the unique name of the status (e.g., "Rejected")
     * @param description a brief description of what this status implies
     */
    @Override
    public void createPaymentStatus(String name, String description) {
        int rowsAffected = jdbcTemplate.update(CREATE_NEW_PAYMENT_STATUS, name, description);
    }

    /**
     * Deletes a specific payment status from the database.
     *
     * @param id the unique identifier of the status to remove
     */
    @Override
    public void deletePaymentStatus(Long id) {
        int rowsAffected = jdbcTemplate.update(
                DELETE_PAYMENT_STATUS,
                id
        );
    }

    /**
     * Updates the details of an existing payment status.
     *
     * @param id          the unique identifier of the status to update
     * @param name        the new name to assign to the status
     * @param description the new description to assign to the status
     */
    @Override
    public void editPaymentStatus(Long id, String name, String description) {
        int rowsAffected = jdbcTemplate.update(
                EDIT_PAYMENT_STATUS,
                name,
                description,
                id
        );
    }
}