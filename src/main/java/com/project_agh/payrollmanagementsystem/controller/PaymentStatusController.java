package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.PaymentStatusDto;
import com.project_agh.payrollmanagementsystem.repositories.PaymentStatusRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for managing payment statuses within the system.
 * <p>
 * This controller provides functionality for creating, deleting, and editing payment statuses.
 * Access is restricted to users with the {@code ROLE_ACCOUNTANT} authority.
 * Operations are mapped to the {@code account/payments/statuses} endpoint.
 * </p>
 */
@Controller
@RequestMapping("accountant/payments/statuses")
@PreAuthorize("hasRole('ROLE_ACCOUNTANT')")
public class PaymentStatusController {

    private final PaymentStatusRepository paymentStatusRepository;

    /**
     * Constructs a new {@code PaymentStatusController} with the required dependency.
     *
     * @param paymentStatusRepository the repository used for payment status persistence operations
     */
    public PaymentStatusController(PaymentStatusRepository paymentStatusRepository) {
        this.paymentStatusRepository = paymentStatusRepository;
    }

    /**
     * Handles the creation of a new payment status.
     * <p>
     * This method accepts form data bound to a {@link PaymentStatusDto}, invokes the
     * repository to create the record, and redirects the user back to the dashboard.
     * </p>
     *
     * @param paymentStatusDto   the DTO containing the name and description of the new status
     * @param redirectAttributes used to pass success or error messages to the redirected view
     * @param request            the current HTTP request
     * @return a redirect string to the statuses tab of the dashboard
     */
    @PostMapping("/create")
    public String createPaymentStatus(
            @ModelAttribute PaymentStatusDto paymentStatusDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            paymentStatusRepository.createPaymentStatus(
                    paymentStatusDto.getName(),
                    paymentStatusDto.getDescription()
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

        return "redirect:/dashboard?tab=statuses";
    }

    /**
     * Handles the deletion of an existing payment status.
     * <p>
     * This method expects a DTO containing the ID of the status to be deleted.
     * Upon success or failure, appropriate feedback messages are added to the redirect attributes.
     * </p>
     *
     * @param paymentStatusDto   the DTO containing the ID of the status to delete
     * @param redirectAttributes used to pass success or error messages to the redirected view
     * @param request            the current HTTP request
     * @return a redirect string to the statuses tab of the dashboard
     */
    @PostMapping("/delete")
    public String deletePaymentStatus(
            @ModelAttribute PaymentStatusDto paymentStatusDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            paymentStatusRepository.deletePaymentStatus(
                    paymentStatusDto.getId()
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

        return "redirect:/dashboard?tab=statuses";
    }

    /**
     * Handles the modification of an existing payment status.
     * <p>
     * This method updates the name and description of a payment status identified by its ID.
     * Parameters are extracted directly from the request.
     * </p>
     *
     * @param id                 the unique identifier of the payment status to edit
     * @param name               the new name for the payment status
     * @param description        the new description for the payment status
     * @param redirectAttributes used to pass success or error messages to the redirected view
     * @return a redirect string to the statuses tab of the dashboard
     */
    @PostMapping("/edit")
    public String editPaymentStatus(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            RedirectAttributes redirectAttributes) {

        try {
            paymentStatusRepository.editPaymentStatus(
                    id, name, description
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "Dane typu (ID: " + id + ") zostały zaktualizowane.");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error editing role: " + e.getMessage());
        }

        return "redirect:/dashboard?tab=statuses";
    }
}