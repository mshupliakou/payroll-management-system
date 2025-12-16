package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.Project;
import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.entities.WorkHours;
import com.project_agh.payrollmanagementsystem.entities.WorkType;
import com.project_agh.payrollmanagementsystem.repositories.WorkHoursRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class JdbcWorkHoursRepository implements WorkHoursRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcWorkHoursRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- SQL QUERIES ---

    // 1. Basic Find All (Using View)
    private static final String FIND_ALL_FROM_VIEW_SQL =
            "SELECT * FROM widok_godzin_pracy ORDER BY data DESC";

    // 2. Accountant Query (With Names & Details)
    // NOTE: This queries the VIEW 'widok_godzin_pracy' 'r' and JOINS 'pracownik' 'p'.
    // Ensure 'widok_godzin_pracy' has 'id_pracownik' column.
    private static final String FIND_BY_DATE_RANGE_SQL =
            "SELECT r.*, " +
                    "       p.imie AS pracownik_imie, " +
                    "       p.nazwisko AS pracownik_nazwisko " +
                    // Note: typ_nazwa and projekt_nazwa are likely already in 'widok_godzin_pracy'
                    // so we don't necessarily need extra joins if the view is good.
                    // But let's keep your join logic just in case the view doesn't have names.
                    "FROM widok_godzin_pracy r " +
                    "JOIN pracownik p ON r.id_pracownik = p.id_pracownik " +
                    "WHERE r.data >= ? AND r.data <= ? " +
                    "ORDER BY r.data ASC";

    private static final String APPROVE_WORK_HOURS =
            "UPDATE rejestracja_godzin_pracy SET zatwierdzenie = true WHERE id_rejestracji = ? ";

    private static final String DELETE_WORKHOURS_SQL =
            "DELETE FROM rejestracja_godzin_pracy WHERE id_rejestracji = ?";

    private static final String EDIT_WORKHOURS =
            "UPDATE rejestracja_godzin_pracy SET data = ?, godzina_rozpoczecia = ?, godzina_zakonczenia = ?, id_typ_pracy = ?, id_projekt = ?, komentarz = ?, zatwierdzenie = ? WHERE id_rejestracji = ?";

    private static final String INSERT_SQL =
            "INSERT INTO rejestracja_godzin_pracy (id_pracownik, data, godzina_rozpoczecia, godzina_zakonczenia, id_typ_pracy, id_projekt, komentarz, zatwierdzenie) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_USER_FROM_VIEW_SQL =
            "SELECT * FROM widok_godzin_pracy WHERE id_pracownik = ? ORDER BY data DESC";

    private static final String FIND_BY_USER_AND_DATE_RANGE_SQL =
            "SELECT * FROM widok_godzin_pracy WHERE id_pracownik = ? AND data >= ? AND data <= ? ORDER BY data ASC";


    // --- ROW MAPPER ---
    private final RowMapper<WorkHours> workHoursRowMapper = (rs, rowNum) -> {
        WorkHours wh = new WorkHours();
        wh.setId(rs.getLong("id_rejestracji"));
        wh.setDate(rs.getDate("data").toLocalDate());
        wh.setStartTime(rs.getTime("godzina_rozpoczecia").toLocalTime());
        wh.setEndTime(rs.getTime("godzina_zakonczenia").toLocalTime());
        wh.setComment(rs.getString("komentarz"));
        wh.setApproved(rs.getBoolean("zatwierdzenie"));

        // View specific columns (Ensure these exist in your view/query result)
        try { wh.setDayOfWeek(rs.getString("dzien_tygodnia")); } catch (SQLException e) {}
        try { wh.setWeekRange(rs.getString("okres_tygodnia")); } catch (SQLException e) {}
        try { wh.setWorkedTime(rs.getString("czas_pracy")); } catch (SQLException e) {}

        // Map WorkType
        WorkType wt = new WorkType();
        wt.setId(rs.getLong("id_typ_pracy"));
        try { wt.setName(rs.getString("typ_nazwa")); } catch (SQLException e) {}
        wh.setWorkType(wt);

        // Map Project
        Long projId = rs.getObject("id_projekt", Long.class);
        if (projId != null) {
            Project p = new Project();
            p.setId(projId);
            try { p.setName(rs.getString("projekt_nazwa")); } catch (SQLException e) {}
            wh.setProject(p);
        }

        // --- MAP USER (FIXED) ---
        User user = new User();
        user.setId(rs.getLong("id_pracownik"));

        // Try reading aliased names first (from Accountant query)
        try {
            String imie = rs.getString("pracownik_imie");
            String nazwisko = rs.getString("pracownik_nazwisko");
            if (imie != null) user.setName(imie);
            if (nazwisko != null) user.setLastname(nazwisko);
        } catch (SQLException e) {
            // If aliases not found, try standard names (if view has them)
            try {
                user.setName(rs.getString("imie"));
                user.setLastname(rs.getString("nazwisko"));
            } catch (SQLException ex) {
                // Ignore if no names found
            }
        }
        wh.setUser(user);

        return wh;
    };


    // --- METHODS ---

    @Override
    public void createWorkHours(Long currentUserId, LocalDate date, Long workType, LocalTime startTime, LocalTime endTime, String comment) {
        jdbcTemplate.update(INSERT_SQL, currentUserId, date, startTime, endTime, workType, null, comment, false);
    }

    @Override
    public void createWorkHoursWithProject(Long currentUserId, LocalDate date, Long workType, Long projectId, LocalTime startTime, LocalTime endTime, String comment) {
        jdbcTemplate.update(INSERT_SQL, currentUserId, date, startTime, endTime, workType, projectId, comment, false);
    }

    @Override
    public List<WorkHours> findByUserId(Long userId) {
        return jdbcTemplate.query(FIND_BY_USER_FROM_VIEW_SQL, workHoursRowMapper, userId);
    }

    @Override
    public List<WorkHours> findAll() {
        return jdbcTemplate.query(FIND_ALL_FROM_VIEW_SQL, workHoursRowMapper);
    }

    @Override
    public void deleteWorkHours(Long id) {
        jdbcTemplate.update(DELETE_WORKHOURS_SQL, id);
    }

    @Override
    public void editWorkHours(Long id, Long currentUserId, LocalDate date, Long workType, LocalTime startTime, LocalTime endTime, String comment) {
        jdbcTemplate.update(EDIT_WORKHOURS, date, startTime, endTime, workType, null, comment, false, id);
    }

    @Override
    public void approveWorkHours(Long id){
        jdbcTemplate.update(APPROVE_WORK_HOURS, id);
    }

    @Override
    public List<WorkHours> findByUserIdAndDateRange(Long id, LocalDate startOfWeek, LocalDate endOfWeek) {
        return jdbcTemplate.query(FIND_BY_USER_AND_DATE_RANGE_SQL, workHoursRowMapper, id, startOfWeek, endOfWeek);
    }

    @Override
    public List<WorkHours> findByDateRange(LocalDate startOfWeek, LocalDate endOfWeek) {
        // Uses the special SQL with JOINs to get names
        return jdbcTemplate.query(FIND_BY_DATE_RANGE_SQL, workHoursRowMapper, startOfWeek, endOfWeek);
    }
}