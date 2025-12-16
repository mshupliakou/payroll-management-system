package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.BankAccountDto;
import com.project_agh.payrollmanagementsystem.dtos.PasswordChangeDto;
import com.project_agh.payrollmanagementsystem.dtos.PhoneNumberDto;
import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller responsible for handling user profile updates such as changing passwords
 * and updating phone numbers. All actions are performed for the currently authenticated user.
 */
@Controller
public class UserInformationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new {@code UserInformationController} with required dependencies.
     *
     * @param userRepository repository for accessing and modifying user data
     * @param passwordEncoder encoder used for validating and hashing passwords
     */
    public UserInformationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Processes the request to change the authenticated user's password.
     * <p>
     * The method performs the following steps:
     * <ul>
     *     <li>Fetches the currently authenticated user.</li>
     *     <li>Validates the current password.</li>
     *     <li>Ensures the new password and its confirmation match.</li>
     *     <li>Encodes and saves the updated password.</li>
     * </ul>
     * Appropriate success or error messages are added to the model before redirecting
     * the user to the dashboard.
     *
     * @param form  DTO containing the old password, new password, and confirmation
     * @param model Spring MVC model used to pass error/success messages
     * @return redirect to the dashboard view
     */
    @PostMapping("/change_password")
    public String changePassword(@ModelAttribute PasswordChangeDto form, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate current password
        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            model.addAttribute("passwordError", "Aktualne hasło jest niepoprawne.");
            return "redirect:/dashboard";
        }

        // Validate new password confirmation
        if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            model.addAttribute("passwordError", "Nowe hasła nie są takie same.");
            return "redirect:/dashboard";
        }

        // Save updated password
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.updatePassword(user.getId(), user.getPassword());

        model.addAttribute("passwordSuccess", "Hasło zostało zmienione pomyślnie.");

        return "redirect:/dashboard";
    }

    /**
     * Processes updating the authenticated user's phone number.
     * <p>
     * The method retrieves the authenticated user, updates the phone number field,
     * and persists the change using the repository.
     *
     * @param form DTO containing the new phone number
     * @param model Spring MVC model (currently unused, but available for messages)
     * @return redirect to the dashboard view
     */
    @PostMapping("/change_phone_number")
    public String changePhoneNumber(@ModelAttribute PhoneNumberDto form, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhone_number(form.getPhoneNumber());
        userRepository.updatePhoneNumber(user.getId(), user.getPhone_number());

        return "redirect:/dashboard";
    }

    @PostMapping("/change_bank_account")
    public String changeBankAccount(@ModelAttribute BankAccountDto form, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setAccount(form.getAccount());
        userRepository.updateAccount(user.getId(), user.getAccount());

        return "redirect:/dashboard";
    }
}
