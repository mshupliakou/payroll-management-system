package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.CreateUserDto;
import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Controller responsible for administrative operations on user accounts.
 * <p>
 * This includes:
 * <ul>
 *     <li>Creating new users</li>
 *     <li>Editing existing user information</li>
 *     <li>Deleting users</li>
 * </ul>
 * All operations require the administrator role ({@code ROLE_ADMIN}).
 * <br><br>
 * Sensitive operations (create/edit) also store audit context such as:
 * <ul>
 *     <li>ID of the administrator performing the action</li>
 *     <li>Client IP address</li>
 * </ul>
 */
@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserManagementController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a controller with required dependencies.
     *
     * @param userRepository repository used for performing user-related database operations
     * @param passwordEncoder encoder used for hashing user passwords
     */
    public UserManagementController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Handles creation of a new user account.
     * <p>
     * The method performs:
     * <ul>
     *     <li>Loading the current administrator performing the action</li>
     *     <li>Hashing the provided password</li>
     *     <li>Passing auditing information (user ID + client IP) to the repository</li>
     *     <li>Delegating actual creation logic to the repository</li>
     * </ul>
     *
     * @param newUserForm       DTO containing all required user creation fields
     * @param redirectAttributes attributes used to pass success/error messages after redirect
     * @param request           HTTP request used to capture administrator IP address
     * @return redirect to the dashboard page
     */
    @PostMapping("/create")
    public String createUser(
            @ModelAttribute CreateUserDto newUserForm,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            User currentUser = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found."));

            Long currentUserId = currentUser.getId();
            String clientIp = request.getRemoteAddr();

            String hashedPassword = passwordEncoder.encode(newUserForm.getPassword());

            userRepository.createUser(
                    newUserForm.getName(),
                    newUserForm.getLastname(),
                    newUserForm.getId_stanowisko(),
                    newUserForm.getId_rola(),
                    newUserForm.getId_dzial(),
                    newUserForm.getWynagrodzenie_pln_g(),
                    newUserForm.getEmail(),
                    null,
                    hashedPassword,
                    newUserForm.getData_zatrudnienia(),
                    null,
                    newUserForm.getAktywny(),
                    currentUserId,
                    clientIp
            );

            redirectAttributes.addFlashAttribute("successMessage", "Użytkownik został utworzony pomyślnie.");

        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating user: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }

    /**
     * Deletes a user by the provided identifier.
     * <p>
     * If the repository throws an exception, an error message is added to the redirect attributes.
     *
     * @param id                 ID of the user to delete
     * @param redirectAttributes redirect message container
     * @return redirect to the dashboard page
     */
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("edit_id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userRepository.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Użytkownik o ID " + id + " został usunięty.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete user: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }

    /**
     * Edits an existing user's information.
     * <p>
     * The logic supports:
     * <ul>
     *     <li>Updating user fields such as name, role, department, email, salary, and dates</li>
     *     <li>Optional password update — hashed only when a non-empty value is provided</li>
     *     <li>Parsing date fields safely, allowing empty termination date</li>
     * </ul>
     *
     * @param id                   ID of the edited user
     * @param name                 new first name
     * @param lastname             new last name
     * @param id_position          new position ID
     * @param id_role              new role ID
     * @param id_department        new department ID
     * @param email                updated email
     * @param salary               updated salary (PLN/g)
     * @param password             new password (optional)
     * @param dataZatrudnieniaStr  hire date (required)
     * @param dataZwolnieniaStr    termination date (optional, may be empty)
     * @param active               whether the user is active
     * @param redirectAttributes   redirect message container
     * @return redirect to dashboard
     */
    @PostMapping("/edit")
    public String editUser(
            @RequestParam("edit_id") Long id,
            @RequestParam("edit_name") String name,
            @RequestParam("edit_lastname") String lastname,
            @RequestParam("edit_id_stanowisko") Long id_position,
            @RequestParam("edit_id_rola") Long id_role,
            @RequestParam("edit_id_dzial") Long id_department,
            @RequestParam("edit_email") String email,
            @RequestParam("edit_wynagrodzenie_pln_g") BigDecimal salary,
            @RequestParam(value = "edit_password", required = false) String password,
            @RequestParam("edit_data_zatrudnienia") String dataZatrudnieniaStr,
            @RequestParam(value = "edit_data_zwolnienia", required = false) String dataZwolnieniaStr,
            @RequestParam(value = "edit_aktywny", defaultValue = "false") boolean active,
            RedirectAttributes redirectAttributes) {

        try {
            LocalDate hireDate = LocalDate.parse(dataZatrudnieniaStr);

            LocalDate terminationDate = null;
            if (dataZwolnieniaStr != null && !dataZwolnieniaStr.isEmpty()) {
                terminationDate = LocalDate.parse(dataZwolnieniaStr);
            }

            if (password != null && !password.trim().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(password);

                userRepository.editUserWithPassword(
                        id, name, lastname, id_position, id_role, id_department,
                        salary, email, hashedPassword, hireDate, terminationDate, active
                );
            } else {
                userRepository.editUser(
                        id, name, lastname, id_position, id_role, id_department,
                        salary, email, hireDate, terminationDate, active
                );
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    "Dane użytkownika (ID: " + id + ") zostały zaktualizowane.");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error editing user: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }
}
