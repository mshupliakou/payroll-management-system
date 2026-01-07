package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.PaymentType;
import com.project_agh.payrollmanagementsystem.repositories.PaymentTypeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of the {@link PaymentTypeRepository}.
 * <p>
 * This repository manages the CRUD operations for {@link PaymentType} entities,
 * allowing administrators to configure the various categories of compensation
 * (e.g., "Regular Salary", "Bonus", "Overtime") directly in the {@code typ_wyplaty} table.
 * </p>
 */
@Repository
public class JdbcPaymentTypeRepository implements PaymentTypeRepository {

    private static final String FIND_ALL_FULL_SQL =
            "SELECT * FROM typ_wyplaty";

    private static final String CREATE_NEW_PAYMENT_TYPE =
            "INSERT INTO typ_wyplaty (nazwa, opis) VALUES (?, ?)";

    private static final String DELETE_PAYMENT_TYPE =
            "DELETE FROM typ_wyplaty \n" +
                    "WHERE id_typ_wyplaty = ?\n";

    private static final String EDIT_PAYMENT_TYPE =
            "UPDATE typ_wyplaty SET " +
                    "nazwa = ?, " +
                    "opis = ? " +
                    "WHERE id_typ_wyplaty = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new {@code JdbcPaymentTypeRepository}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used for executing SQL queries
     */
    public JdbcPaymentTypeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Maps database rows from the 'typ_wyplaty' table to {@link PaymentType} objects.
     */
    private final RowMapper<PaymentType> paymentTypeRowMapper = (rs, rowNum) -> {
        PaymentType paymentType = new PaymentType();
        paymentType.setId(rs.getLong("id_typ_wyplaty"));
        paymentType.setName(rs.getString("nazwa"));
        paymentType.setDescription(rs.getString("opis"));
        return paymentType;
    };

    /**
     * Retrieves all defined payment types from the database.
     *
     * @return a {@link List} of all {@link PaymentType} entities
     */
    @Override
    public List<PaymentType> findAll() {
        return jdbcTemplate.query(FIND_ALL_FULL_SQL, paymentTypeRowMapper);
    }

    /**
     * Creates and persists a new payment type definition.
     *
     * @param name        the unique name of the payment type (e.g., "Bonus")
     * @param description a brief description of the payment type's purpose
     */
    @Override
    public void createPaymentType(String name, String description) {
        int rowsAffected = jdbcTemplate.update(CREATE_NEW_PAYMENT_TYPE, name, description);

        if (rowsAffected != 1) {
            // Optional: handle the error if the row was not inserted
        }
    }

    /**
     * Deletes a specific payment type from the database.
     *
     * @param id the unique identifier of the payment type to remove
     */
    @Override
    public void deletePaymentType(Long id) {
        int rowsAffected = jdbcTemplate.update(
                DELETE_PAYMENT_TYPE,
                id
        );

        if (rowsAffected != 1) {
            // Optional: Handle case where delete failed
        }
    }

    /**
     * Updates the details of an existing payment type.
     *
     * @param id          the unique identifier of the payment type to update
     * @param name        the new name to assign to the payment type
     * @param description the new description to assign to the payment type
     */
    @Override
    public void editPaymentType(Long id, String name, String description) {
        int rowsAffected = jdbcTemplate.update(
                EDIT_PAYMENT_TYPE,
                name,
                description,
                id
        );
        if (rowsAffected != 1) {
            // Optional: Handle case where update failed
        }
    }
}