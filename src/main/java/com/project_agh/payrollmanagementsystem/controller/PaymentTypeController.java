package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.PaymentTypeDto;
import com.project_agh.payrollmanagementsystem.repositories.PaymentTypeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for managing the various payment types available in the system.
 * <p>
 * This controller allows users with the {@code ROLE_ACCOUNTANT} authority to create, delete,
 * and modify payment types (e.g., standard salary, bonus, overtime). All operations are
 * mapped under the {@code accountant/payments/types} endpoint.
 * </p>
 */
@Controller
@RequestMapping("accountant/payments/types")
@PreAuthorize("hasRole('ROLE_ACCOUNTANT')")
public class PaymentTypeController {

    private final PaymentTypeRepository paymentTypeRepository;

    /**
     * Constructs a new {@code PaymentTypeController} with the required dependency.
     *
     * @param paymentTypeRepository the repository used for payment type persistence operations
     */
    public PaymentTypeController(PaymentTypeRepository paymentTypeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
    }

    /**
     * Handles the creation of a new payment type.
     * <p>
     * Takes form data from the {@link PaymentTypeDto}, persists the new type using the repository,
     * and provides feedback via flash attributes.
     * </p>
     *
     * @param paymetTypeDto      the DTO containing the name and description of the new payment type
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @param request            the current HTTP request
     * @return a redirect string to the types tab of the dashboard
     */
    @PostMapping("/create")
    public String createPaymentType(
            @ModelAttribute PaymentTypeDto paymetTypeDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            paymentTypeRepository.createPaymentType(
                    paymetTypeDto.getName(),
                    paymetTypeDto.getDescription()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Payment Type has been created successfully."
            );

        } catch (Exception e) {
            System.err.println("Error creating role: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to create role. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=types";
    }

    /**
     * Handles the deletion of a payment type.
     * <p>
     * Deletes the payment type identified by the ID within the provided DTO.
     * </p>
     *
     * @param paymetTypeDto      the DTO containing the ID of the payment type to delete
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @param request            the current HTTP request
     * @return a redirect string to the types tab of the dashboard
     */
    @PostMapping("/delete")
    public String deletePaymentType(
            @ModelAttribute PaymentTypeDto paymetTypeDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            paymentTypeRepository.deletePaymentType(
                    paymetTypeDto.getId()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "PaymentType has been deleted successfully."
            );

        } catch (Exception e) {
            System.err.println("Error deleting paymnet type: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to delete type. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=types";
    }

    /**
     * Handles the modification of an existing payment type.
     * <p>
     * Updates the name and description of the payment type identified by the provided ID.
     * </p>
     *
     * @param id                 the unique identifier of the payment type to edit
     * @param name               the new name for the payment type
     * @param description        the new description for the payment type
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @return a redirect string to the types tab of the dashboard
     */
    @PostMapping("/edit")
    public String editPaymentType(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            RedirectAttributes redirectAttributes) {

        try {
            paymentTypeRepository.editPaymentType(
                    id, name, description
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "Dane typu (ID: " + id + ") zostały zaktualizowane.");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error editing role: " + e.getMessage());
        }

        return "redirect:/dashboard?tab=types";
    }
}