package com.project_agh.payrollmanagementsystem.repositories.jdbc;

import com.project_agh.payrollmanagementsystem.entities.*;
import com.project_agh.payrollmanagementsystem.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcWorkHoursRepository workHoursRepository;
    private final JdbcTemplate jdbcTemplate;


    private static final String FIND_BY_EMAIL_SQL =
            "SELECT " +
                    "    p.id_pracownik, p.imie, p.nazwisko, p.wynagrodzenie_pln_g, p.email, p.telefon, p.haslo_hash, " +
                    "    p.data_zatrudnienia, p.data_zwolnienia, p.aktywny, p.konto_bankowe, " + // <--- ДОБАВЛЕНО
                    "    s.id_stanowisko AS stanowisko_id, s.nazwa AS stanowisko_nazwa, s.opis AS stanowisko_opis, " +
                    "    r.id_rola AS rola_id, r.nazwa AS rola_nazwa, " +
                    "    d.id_dzial AS dzial_id, d.nazwa AS dzial_nazwa, d.opis AS dzial_opis " +
                    "FROM pracownik p " +
                    "JOIN stanowisko s ON s.id_stanowisko = p.id_stanowisko " +
                    "JOIN rola r ON r.id_rola = p.id_rola " +
                    "JOIN dzial d ON d.id_dzial = p.id_dzial " +
                    "WHERE p.email = ?";

    private static final String FIND_BY_ID_SQL =
            "SELECT " +
                    "    p.id_pracownik, p.imie, p.nazwisko, p.wynagrodzenie_pln_g, p.email, p.telefon, p.haslo_hash, " +
                    "    p.data_zatrudnienia, p.data_zwolnienia, p.aktywny, p.konto_bankowe, " + // <--- ДОБАВЛЕНО
                    "    s.id_stanowisko AS stanowisko_id, s.nazwa AS stanowisko_nazwa, s.opis AS stanowisko_opis, " +
                    "    r.id_rola AS rola_id, r.nazwa AS rola_nazwa, " +
                    "    d.id_dzial AS dzial_id, d.nazwa AS dzial_nazwa, d.opis AS dzial_opis " +
                    "FROM pracownik p " +
                    "JOIN stanowisko s ON s.id_stanowisko = p.id_stanowisko " +
                    "JOIN rola r ON r.id_rola = p.id_rola " +
                    "JOIN dzial d ON d.id_dzial = p.id_dzial " +
                    "WHERE p.id_pracownik = ?";


    private static final String FIND_ALL_FULL_SQL =
            "SELECT " +
                    "    p.id_pracownik, p.imie, p.nazwisko,  p.wynagrodzenie_pln_g, p.email, p.telefon, p.haslo_hash, " +
                    "    p.data_zatrudnienia, p.data_zwolnienia, p.aktywny, p.konto_bankowe, " +
                    "    s.id_stanowisko AS stanowisko_id, s.nazwa AS stanowisko_nazwa, s.opis AS stanowisko_opis, " +
                    "    r.id_rola AS rola_id, r.nazwa AS rola_nazwa, " +
                    "    d.id_dzial AS dzial_id, d.nazwa AS dzial_nazwa, d.opis AS dzial_opis " +
                    "FROM pracownik p " +
                    "JOIN stanowisko s ON s.id_stanowisko = p.id_stanowisko " +
                    "JOIN rola r ON r.id_rola = p.id_rola " +
                    "JOIN dzial d ON d.id_dzial = p.id_dzial";

    private static final String FIND_NOT_APPROVED_SQL =
            "SELECT DISTINCT " +
                    "    p.id_pracownik, p.imie, p.nazwisko, p.wynagrodzenie_pln_g, p.email, p.telefon, p.haslo_hash, " +
                    "    p.data_zatrudnienia, p.data_zwolnienia, p.aktywny, p.konto_bankowe, " +
                    "    s.id_stanowisko AS stanowisko_id, s.nazwa AS stanowisko_nazwa, s.opis AS stanowisko_opis, " +
                    "    r.id_rola AS rola_id, r.nazwa AS rola_nazwa, " +
                    "    d.id_dzial AS dzial_id, d.nazwa AS dzial_nazwa, d.opis AS dzial_opis " +

                    "FROM pracownik p " +
                    "JOIN stanowisko s ON s.id_stanowisko = p.id_stanowisko " +
                    "JOIN rola r ON r.id_rola = p.id_rola " +
                    "JOIN dzial d ON d.id_dzial = p.id_dzial " +
                    "JOIN rejestracja_godzin_pracy rej ON rej.id_pracownik = p.id_pracownik " +
                    "WHERE rej.zatwierdzenie = false";


    private static final String CREATE_NEW_USER=
            "insert into pracownik (imie, nazwisko, id_stanowisko, id_rola, id_dzial, wynagrodzenie_pln_g, " +
                    "email, telefon, haslo_hash, data_zatrudnienia, data_zwolnienia, aktywny) values(?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String EDIT_USER_WITH_PASSWORD =
            "UPDATE pracownik SET " +
                    "imie = ?, " +
                    "nazwisko = ?, " +
                    "id_stanowisko = ?, " +
                    "id_rola = ?, " +
                    "id_dzial = ?, " +
                    "wynagrodzenie_pln_g = ?, " +
                    "email = ?, " +
                    "haslo_hash = ?, " +
                    "data_zatrudnienia = ?, " +
                    "data_zwolnienia = ?, " +
                    "aktywny = ? " +
                    "WHERE id_pracownik = ?";

    private static final String EDIT_USER=
            "UPDATE pracownik SET " +
                    "imie = ?, " +
                    "nazwisko = ?, " +
                    "id_stanowisko = ?, " +
                    "id_rola = ?, " +
                    "id_dzial = ?, " +
                    "wynagrodzenie_pln_g = ?, " +
                    "email = ?, " +
                    "data_zatrudnienia = ?, " +
                    "data_zwolnienia = ?, " +
                    "aktywny = ? " +
                    "WHERE id_pracownik = ?";




    private static final String CHANGE_PASSWORD =
            "UPDATE pracownik \n" +
                    "SET haslo_hash = ? \n" +
                    "WHERE id_pracownik = ?\n";

    private static final String CHANGE_PHONE_NUMBER =
            "UPDATE pracownik \n" +
                    "SET telefon = ? \n" +
                    "WHERE id_pracownik = ?\n";
    private static final String CHANGE_ACCOUNT =
            "UPDATE pracownik \n" +
            "SET konto_bankowe = ? \n" +
            "WHERE id_pracownik = ?\n";

    private static final String DELETE_USER =
            "DELETE FROM  pracownik \n" +
                    "WHERE id_pracownik = ?\n";

    public JdbcUserRepository(@Lazy JdbcWorkHoursRepository workHoursRepository, JdbcTemplate jdbcTemplate) {
        this.workHoursRepository = workHoursRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();


        user.setId(rs.getLong("id_pracownik"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("imie"));
        user.setLastname(rs.getString("nazwisko"));
        user.setSalary_pln_h(rs.getBigDecimal("wynagrodzenie_pln_g"));
        user.setPhone_number(rs.getString("telefon"));
        user.setPassword(rs.getString("haslo_hash"));
        user.setActive(rs.getBoolean("aktywny"));


        Date hireSqlDate = rs.getDate("data_zatrudnienia");
        if (hireSqlDate != null) {
            user.setHireDate(hireSqlDate.toLocalDate());
        } else {
            user.setHireDate(LocalDate.MIN);
        }


        Date retirementSqlDate = rs.getDate("data_zwolnienia");
        if (retirementSqlDate != null) {
            user.setRetirementDate(retirementSqlDate.toLocalDate());
        } else {
            user.setRetirementDate(null);
        }


        Position position = new Position();
        position.setId(rs.getLong("stanowisko_id"));
        position.setName(rs.getString("stanowisko_nazwa"));
        position.setDescription(rs.getString("stanowisko_opis"));
        user.setPosition(position);


        Role role = new Role();
        role.setId(rs.getLong("rola_id"));
        role.setName(rs.getString("rola_nazwa"));
        user.setRole(role);


        Department department = new Department();
        department.setId(rs.getLong("dzial_id"));
        department.setName(rs.getString("dzial_nazwa"));
        department.setDescription(rs.getString("dzial_opis"));
        user.setDepartment(department);

        user.setAccount(rs.getString("konto_bankowe"));

        return user;
    };

    public RowMapper<User> getUserRowMapper() {
        return userRowMapper;
    }

    @Override
    public void deleteUser(Long id){
        int rowsAffected = jdbcTemplate.update(
                DELETE_USER,
                id
        );

        if (rowsAffected != 1) {
        }
    }

    @Override
    public void editUserWithPassword(Long id, String imie, String nazwisko, Long id_stanowisko, Long id_rola,
                         Long id_dzial, BigDecimal wynagrodzenie_pln_g
            , String email,  String haslo_hash, LocalDate data_zatrudnienia,
                         LocalDate data_zwolnienia, boolean aktywny){

        Date sqlHireDate = Date.valueOf(data_zatrudnienia);
        Date sqlRetirementDate = (data_zwolnienia != null) ? Date.valueOf(data_zwolnienia) : null;

        int rowsAffected = jdbcTemplate.update(
                EDIT_USER_WITH_PASSWORD,
                imie,
                nazwisko,
                id_stanowisko,
                id_rola,
                id_dzial,
                wynagrodzenie_pln_g,
                email,
                haslo_hash,
                sqlHireDate,
                sqlRetirementDate,
                aktywny,
                id
        );


        if (rowsAffected != 1) {

        }
    }

    @Override
    public void editUser(Long id, String imie, String nazwisko, Long id_stanowisko, Long id_rola, Long id_dzial, BigDecimal wynagrodzenie_pln_g, String email, LocalDate data_zatrudnienia, LocalDate data_zwolnienia, boolean aktywny) {

        Date sqlHireDate = Date.valueOf(data_zatrudnienia);
        Date sqlRetirementDate = (data_zwolnienia != null) ? Date.valueOf(data_zwolnienia) : null;


        int rowsAffected = jdbcTemplate.update(
                EDIT_USER,
                imie,
                nazwisko,
                id_stanowisko,
                id_rola,
                id_dzial,
                wynagrodzenie_pln_g,
                email,
                sqlHireDate,
                sqlRetirementDate,
                aktywny,
                id
        );

        if (rowsAffected != 1) {

        }
    }

    @Override
    public Optional<User> findById(long userId) {
        List<User> results = jdbcTemplate.query(
                FIND_BY_ID_SQL,
                userRowMapper,
                userId
        );


        return results.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> results = jdbcTemplate.query(
                FIND_BY_EMAIL_SQL,
                userRowMapper,
                email
        );


        return results.stream().findFirst();
    }

    public void updatePhoneNumber(Long id, String phoneNumber){
        int rowsAffected = jdbcTemplate.update(
                CHANGE_PHONE_NUMBER,
                phoneNumber,
                id

        );

        if (rowsAffected != 1) {

        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = jdbcTemplate.query(FIND_ALL_FULL_SQL, userRowMapper);
        for (User user : users) {
            List<WorkHours> hours = workHoursRepository.findByUserId(user.getId());
            user.setWorkHours(hours);
        }

        return users;
    }

    @Override
    public void updatePassword(Long id, String newPassword){
        int rowsAffected = jdbcTemplate.update(
                CHANGE_PASSWORD,
                newPassword,
                id

        );
        if (rowsAffected != 1) {
        }
    }

    @Override
    public void createUser(String imie, String nazwisko, Long id_stanowisko, Long id_rola,
                           Long id_dzial, BigDecimal wynagrodzenie_pln_g
            , String email, String telefon, String haslo_hash, LocalDate data_zatrudnienia,
                           LocalDate data_zwolnienia, boolean aktywny, Long currentUserId, String clientIp) {


        String userIdSql = "SET app.current_user_id = '" + currentUserId.toString() + "'";
    jdbcTemplate.execute(userIdSql);
        String clientIpSql = "SET app.client_ip = '" + clientIp + "'";
    jdbcTemplate.execute(clientIpSql);


        Date sqlHireDate = Date.valueOf(data_zatrudnienia);
        Date sqlRetirementDate = (data_zwolnienia != null) ? Date.valueOf(data_zwolnienia) : null;

        int rowsAffected = jdbcTemplate.update(
                CREATE_NEW_USER,
                imie,
                nazwisko,
                id_stanowisko,
                id_rola,
                id_dzial,
                wynagrodzenie_pln_g,
                email,
                telefon,
                haslo_hash,
                sqlHireDate,
                sqlRetirementDate,
                aktywny
        );

    }

    @Override
    public void updateAccount(Long id, String account) {
        int rowsAffected = jdbcTemplate.update(
                CHANGE_ACCOUNT,
                account,
                id

        );

    }

    @Override
    public List<User> findAllNotApproved() {
        List<User> users = jdbcTemplate.query(FIND_NOT_APPROVED_SQL, userRowMapper);

        for (User user : users) {
            List<WorkHours> hours = workHoursRepository.findByUserId(user.getId());
            user.setWorkHours(hours);
        }

        return users;
    }

}