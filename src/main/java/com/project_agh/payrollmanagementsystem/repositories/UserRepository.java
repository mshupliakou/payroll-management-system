package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations and
 * specific queries on {@link User} entities.
 * <p>
 * Contains methods for creating, editing, deleting, and retrieving users,
 * as well as updating sensitive fields such as passwords and phone numbers.
 * This repository also supports passing audit information (user ID and IP) for create operations.
 * </p>
 */
public interface UserRepository {

    /**
     * Retrieves a user by their unique email address.
     * <p>
     * Essential for authentication, as the email is used as the login identifier.
     *
     * @param email the unique email address of the user
     * @return an {@link Optional} containing the matching {@link User} if found, or empty if not
     */
    Optional<User> findByEmail(String email);

    /**
     * Updates all user information including the password.
     *
     * @param id               ID of the user to update
     * @param imie             new first name
     * @param nazwisko         new last name
     * @param id_stanowisko    new position ID
     * @param id_rola          new role ID
     * @param id_dzial         new department ID
     * @param wynagrodzenie_pln_g updated salary
     * @param email            new email
     * @param haslo_hash       new hashed password
     * @param data_zatrudnienia hire date
     * @param data_zwolnienia  termination date (optional)
     * @param aktywny          whether the user is active
     */
    void editUserWithPassword(Long id, String imie, String nazwisko, Long id_stanowisko, Long id_rola,
                              Long id_dzial, BigDecimal wynagrodzenie_pln_g,
                              String email, String haslo_hash, LocalDate data_zatrudnienia,
                              LocalDate data_zwolnienia, boolean aktywny);

    /**
     * Updates user information without modifying the password.
     *
     * @param id               ID of the user to update
     * @param imie             new first name
     * @param nazwisko         new last name
     * @param id_stanowisko    new position ID
     * @param id_rola          new role ID
     * @param id_dzial         new department ID
     * @param wynagrodzenie_pln_g updated salary
     * @param email            new email
     * @param data_zatrudnienia hire date
     * @param data_zwolnienia  termination date (optional)
     * @param aktywny          whether the user is active
     */
    void editUser(Long id, String imie, String nazwisko, Long id_stanowisko, Long id_rola,
                  Long id_dzial, BigDecimal wynagrodzenie_pln_g,
                  String email, LocalDate data_zatrudnienia,
                  LocalDate data_zwolnienia, boolean aktywny);

    /**
     * Retrieves all users in the system.
     *
     * @return a {@link List} of all {@link User} entities
     */
    List<User> findAll();

    /**
     * Updates the password of the user with the specified ID.
     *
     * @param id          ID of the user
     * @param newPassword new hashed password to set
     */
    void updatePassword(Long id, String newPassword);

    /**
     * Updates the phone number of the user with the specified ID.
     *
     * @param id          ID of the user
     * @param phoneNumber new phone number to set
     */
    void updatePhoneNumber(Long id, String phoneNumber);

    /**
     * Deletes the user with the specified ID from the system.
     *
     * @param id ID of the user to delete
     */
    void deleteUser(Long id);

    /**
     * Creates a new user in the system, including optional audit information.
     *
     * @param imie             first name
     * @param nazwisko         last name
     * @param id_stanowisko    position ID
     * @param id_rola          role ID
     * @param id_dzial         department ID
     * @param wynagrodzenie_pln_g salary
     * @param email            email address
     * @param telefon          phone number (optional)
     * @param haslo_hash       hashed password
     * @param data_zatrudnienia hire date
     * @param data_zwolnienia  termination date (optional)
     * @param aktywny          whether the user is active
     * @param currentUserId    ID of the administrator performing the creation
     * @param clientIp         IP address of the client performing the creation
     */
    void createUser(String imie, String nazwisko, Long id_stanowisko, Long id_rola, Long id_dzial,
                    BigDecimal wynagrodzenie_pln_g, String email, String telefon, String haslo_hash,
                    LocalDate data_zatrudnienia, LocalDate data_zwolnienia, boolean aktywny,
                    Long currentUserId, String clientIp);

    void updateAccount(Long id, String account);

    List<User> findAllNotApproved();
}
