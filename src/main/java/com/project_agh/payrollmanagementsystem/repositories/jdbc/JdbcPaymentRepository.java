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

@Repository
public class JdbcPaymentRepository implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    // --- SQL Z ALIASAMI (typ_nazwa, status_nazwa) ---
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

    public JdbcPaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- ROW MAPPER ---
    private final RowMapper<Payment> paymentRowMapper = (rs, rowNum) -> {
        Payment payment = new Payment();

        // 1. Podstawowe dane wypłaty
        payment.setId(rs.getLong("id_wyplata"));
        payment.setAmount(rs.getBigDecimal("wyplata"));

        java.sql.Timestamp ts = rs.getTimestamp("data");
        if (ts != null) {
            payment.setDate(LocalDate.from(ts.toLocalDateTime()));
        }
        payment.setDescription(rs.getString("opis"));

        // 2. Mapowanie Pracownika (User)
        User user = new User();
        user.setId(rs.getLong("id_pracownik"));
        user.setName(rs.getString("imie"));
        user.setLastname(rs.getString("nazwisko"));
        user.setEmail(rs.getString("email"));
        payment.setUser(user);

        // 3. Mapowanie Typu Wypłaty (UŻYWAMY ALIASU Z SQL!)
        PaymentType type = new PaymentType();
        type.setId(rs.getLong("id_typ_wyplaty"));
        // WAŻNE: Tu było rs.getString("nazwa") -> BŁĄD. Musi być alias:
        type.setName(rs.getString("typ_nazwa"));
        payment.setPaymentType(type);

        // 4. Mapowanie Statusu Wypłaty (UŻYWAMY ALIASU Z SQL!)
        PaymentStatus status = new PaymentStatus();
        status.setId(rs.getLong("id_status_wyplaty"));
        // WAŻNE: Tu też musi być alias:
        status.setName(rs.getString("status_nazwa"));
        payment.setPaymentStatus(status);

        return payment;
    };

    @Override
    public List<Payment> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, paymentRowMapper);
    }

    @Override
    public void createPayment() {

    }

    @Override
    public void deletePayment(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public void editPayment() {

    }

    @Override
    public void updateStatus(Long id, Long statusId) {
        jdbcTemplate.update(UPDATE_STATUS_SQL, statusId, id);
    }
}