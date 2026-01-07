package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.Payment;
import com.project_agh.payrollmanagementsystem.entities.PaymentStatus;
import com.project_agh.payrollmanagementsystem.entities.PaymentType;
import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.repositories.PaymentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * JDBC-based implementation of the {@link PaymentRepository}.
 * <p>
 * This repository manages the retrieval and modification of payment records.
 * It utilizes complex SQL joins to hydrate the {@link Payment} entity with associated
 * {@link User}, {@link PaymentType}, and {@link PaymentStatus} objects in a single query.
 * </p>
 */
@Repository
public class JdbcPaymentRepository implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    // --- SQL WITH ALIASES (typ_nazwa, status_nazwa) ---
    // Aliases are required because both PaymentType and PaymentStatus have a column named 'nazwa'.
    private static final String FIND_ALL_SQL = """
            SELECT h.id_wyplata, h.wyplata, h.data, h.opis,
                   p.id_pracownik, p.imie, p.nazwisko, p.email,
                   t.id_typ_wyplaty, t.nazwa AS typ_nazwa,
                   s.id_status_wyplaty, s.nazwa AS status_nazwa
            FROM historia_wyplat h
            JOIN pracownik p ON h.id_pracownik = p.id_pracownik
            LEFT JOIN typ_wyplaty t ON h.id_typ_wyplaty = t.id_typ_wyplaty
            LEFT JOIN status_wyplaty s ON h.id_status_wyplaty = s.id_status_wyplaty
            ORDER BY h.data DESC
            """;

    private static final String DELETE_SQL = "DELETE FROM historia_wyplat WHERE id_wyplata = ?";
    private static final String UPDATE_STATUS_SQL = "UPDATE historia_wyplat SET id_status_wyplaty = ? WHERE id_wyplata = ?";

    /**
     * Constructs a new {@code JdbcPaymentRepository}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used for database operations
     */
    public JdbcPaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- ROW MAPPER ---
    /**
     * Maps the result set to a {@link Payment} object.
     * <p>
     * Handles the construction of nested objects (User, Type, Status).
     * <b>Note:</b> Explicit column aliases ('typ_nazwa', 'status_nazwa') are used to avoid naming collisions.
     * </p>
     */
    private final RowMapper<Payment> paymentRowMapper = (rs, rowNum) -> {
        Payment payment = new Payment();

        // 1. Basic payment data
        payment.setId(rs.getLong("id_wyplata"));
        payment.setAmount(rs.getBigDecimal("wyplata"));

        java.sql.Timestamp ts = rs.getTimestamp("data");
        if (ts != null) {
            payment.setDate(LocalDate.from(ts.toLocalDateTime()));
        }
        payment.setDescription(rs.getString("opis"));

        // 2. Mapping Employee (User)
        User user = new User();
        user.setId(rs.getLong("id_pracownik"));
        user.setName(rs.getString("imie"));
        user.setLastname(rs.getString("nazwisko"));
        user.setEmail(rs.getString("email"));
        payment.setUser(user);

        // 3. Mapping Payment Type (USING SQL ALIAS)
        PaymentType type = new PaymentType();
        type.setId(rs.getLong("id_typ_wyplaty"));
        // IMPORTANT: Must use alias 'typ_nazwa' instead of 'nazwa'
        type.setName(rs.getString("typ_nazwa"));
        payment.setPaymentType(type);

        // 4. Mapping Payment Status (USING SQL ALIAS)
        PaymentStatus status = new PaymentStatus();
        status.setId(rs.getLong("id_status_wyplaty"));
        // IMPORTANT: Must use alias 'status_nazwa' instead of 'nazwa'
        status.setName(rs.getString("status_nazwa"));
        payment.setPaymentStatus(status);

        return payment;
    };

    /**
     * Retrieves the complete history of payments.
     * <p>
     * The results include joined data for the employee, payment type, and payment status,
     * ordered by date descending.
     * </p>
     *
     * @return a list of all {@link Payment} entities
     */
    @Override
    public List<Payment> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, paymentRowMapper);
    }

    /**
     * Placeholder for creating a new payment.
     * <p>
     * Currently not implemented in this repository.
     * </p>
     */
    @Override
    public void createPayment() {
        // Implementation pending
    }

    /**
     * Deletes a specific payment record from the history.
     *
     * @param id the unique identifier of the payment to delete
     */
    @Override
    public void deletePayment(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    /**
     * Placeholder for editing payment details.
     * <p>
     * Currently not implemented in this repository.
     * </p>
     */
    @Override
    public void editPayment() {
        // Implementation pending
    }

    /**
     * Updates the status of a specific payment.
     * <p>
     * This is typically used to move a payment from "Pending" to "Approved" or "Paid".
     * </p>
     *
     * @param id       the unique identifier of the payment record
     * @param statusId the unique identifier of the new status to apply
     */
    @Override
    public void updateStatus(Long id, Long statusId) {
        jdbcTemplate.update(UPDATE_STATUS_SQL, statusId, id);
    }
}