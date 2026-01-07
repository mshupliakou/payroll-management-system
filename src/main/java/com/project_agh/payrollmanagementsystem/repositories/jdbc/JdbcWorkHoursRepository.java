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

/**
 * JDBC-based implementation of the {@link WorkHoursRepository}.
 * <p>
 * This repository manages the persistence of work time entries. It heavily relies on the
 * database view {@code widok_godzin_pracy} for retrieval operations. This view pre-calculates
 * derived data (like duration strings, day names) and joins necessary reference tables,
 * simplifying the Java-side logic for generating reports and timesheets.
 * </p>
 */
@Repository
public class JdbcWorkHoursRepository implements WorkHoursRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new {@code JdbcWorkHoursRepository}.
     *
     * @param jdbcTemplate the {@link JdbcTemplate} used for executing SQL queries
     */
    public JdbcWorkHoursRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- SQL QUERIES ---

    // 1. Basic Find All (Using View)
    // Retrieves all records from the view, joining the employee table to get names.
    private static final String FIND_ALL_FROM_VIEW_SQL =
            "SELECT r.*, " +
                    "       p.imie AS pracownik_imie, " +
                    "       p.nazwisko AS pracownik_nazwisko " +
                    "FROM widok_godzin_pracy r " +
                    "JOIN pracownik p ON r.id_pracownik = p.id_pracownik " +
                    "ORDER BY r.data DESC";

    // 2. Accountant/Report Query
    // Retrieves records within a date range. It explicitly selects employee names
    // via aliases (pracownik_imie, pracownik_nazwisko) to ensure the RowMapper finds them.
    private static final String FIND_BY_DATE_RANGE_SQL =
            "SELECT r.*, " +
                    "       p.imie AS pracownik_imie, " +
                    "       p.nazwisko AS pracownik_nazwisko " +
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
    /**
     * Maps database rows to {@link WorkHours} objects.
     * <p>
     * <b>Robustness Strategy:</b> This mapper uses {@code try-catch} blocks for certain columns
     * (like aliased names or view-specific calculated fields). This allows the same mapper to be
     * reused across different queries where some of these auxiliary columns might be missing,
     * preventing {@code SQLException} from crashing the operation.
     * </p>
     */
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

        // Map Project (Optional)
        Long projId = rs.getObject("id_projekt", Long.class);
        if (projId != null) {
            Project p = new Project();
            p.setId(projId);
            try { p.setName(rs.getString("projekt_nazwa")); } catch (SQLException e) {}
            wh.setProject(p);
        }

        // --- MAP USER ---
        User user = new User();
        user.setId(rs.getLong("id_pracownik"));

        // Try reading aliased names first (from Accountant query)
        try {
            String imie = rs.getString("pracownik_imie");
            String nazwisko = rs.getString("pracownik_nazwisko");
            if (imie != null) user.setName(imie);
            if (nazwisko != null) user.setLastname(nazwisko);
        } catch (SQLException e) {
            // If aliases not found, try standard names (fallback if view has them)
            try {
                user.setName(rs.getString("imie"));
                user.setLastname(rs.getString("nazwisko"));
            } catch (SQLException ex) {
                // Ignore if no names found; we still have the ID
            }
        }
        wh.setUser(user);

        return wh;
    };


    // --- METHODS ---

    /**
     * Creates a general work hour entry (not linked to a specific project).
     *
     * @param currentUserId the ID of the employee
     * @param date          the date of work
     * @param workType      the ID of the work type (e.g., Remote)
     * @param startTime     start time
     * @param endTime       end time
     * @param comment       optional comment
     */
    @Override
    public void createWorkHours(Long currentUserId, LocalDate date, Long workType, LocalTime startTime, LocalTime endTime, String comment) {
        jdbcTemplate.update(INSERT_SQL, currentUserId, date, startTime, endTime, workType, null, comment, false);
    }

    /**
     * Creates a work hour entry linked to a specific project.
     *
     * @param currentUserId the ID of the employee
     * @param date          the date of work
     * @param workType      the ID of the work type
     * @param projectId     the ID of the project
     * @param startTime     start time
     * @param endTime       end time
     * @param comment       optional comment
     */
    @Override
    public void createWorkHoursWithProject(Long currentUserId, LocalDate date, Long workType, Long projectId, LocalTime startTime, LocalTime endTime, String comment) {
        jdbcTemplate.update(INSERT_SQL, currentUserId, date, startTime, endTime, workType, projectId, comment, false);
    }

    /**
     * Retrieves all work hours for a specific user.
     *
     * @param userId the ID of the user
     * @return list of work hours
     */
    @Override
    public List<WorkHours> findByUserId(Long userId) {
        return jdbcTemplate.query(FIND_BY_USER_FROM_VIEW_SQL, workHoursRowMapper, userId);
    }

    /**
     * Retrieves all work hours in the system (Admin/Manager view).
     *
     * @return list of all work hours
     */
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
        // Note: Approval is reset to false upon edit
        jdbcTemplate.update(EDIT_WORKHOURS, date, startTime, endTime, workType, null, comment, false, id);
    }

    /**
     * Marks a specific work hour record as approved.
     *
     * @param id the ID of the record to approve
     */
    @Override
    public void approveWorkHours(Long id){
        jdbcTemplate.update(APPROVE_WORK_HOURS, id);
    }

    /**
     * Retrieves work hours for a specific user within a date range.
     *
     * @param id          the user ID
     * @param startOfWeek range start date
     * @param endOfWeek   range end date
     * @return filtered list of work hours
     */
    @Override
    public List<WorkHours> findByUserIdAndDateRange(Long id, LocalDate startOfWeek, LocalDate endOfWeek) {
        return jdbcTemplate.query(FIND_BY_USER_AND_DATE_RANGE_SQL, workHoursRowMapper, id, startOfWeek, endOfWeek);
    }

    /**
     * Retrieves work hours for ALL users within a date range.
     * <p>
     * Used primarily for generating payroll reports for a specific period.
     * </p>
     *
     * @param startOfWeek range start date
     * @param endOfWeek   range end date
     * @return filtered list of work hours
     */
    @Override
    public List<WorkHours> findByDateRange(LocalDate startOfWeek, LocalDate endOfWeek) {
        return jdbcTemplate.query(FIND_BY_DATE_RANGE_SQL, workHoursRowMapper, startOfWeek, endOfWeek);
    }
}