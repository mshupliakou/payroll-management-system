package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.BulkStatusUpdateDto;
import com.project_agh.payrollmanagementsystem.repositories.PaymentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller responsible for handling payment-related operations for accountants.
 * <p>
 * This controller manages the deletion and status updates of payment records,
 * including both single-record operations and bulk actions. It maps requests
 * starting with {@code /accountant/payments}.
 * </p>
 */
@Controller
@RequestMapping("/accountant/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;

    /**
     * Constructs a new {@code PaymentController} with the necessary repository.
     *
     * @param paymentRepository the repository used for payment persistence operations
     */
    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Deletes a single payment record based on the provided ID.
     * <p>
     * If the deletion is successful, a success message is added to the flash attributes.
     * If an error occurs, an error message is added instead.
     * </p>
     *
     * @param id                 the unique identifier of the payment to be deleted
     * @param redirectAttributes used to pass success or error messages to the redirected view
     * @return a redirect string to the salaries tab of the dashboard
     */
    @PostMapping("/delete")
    public String deletePayment(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            paymentRepository.deletePayment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Wypłata została usunięta.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd usuwania: " + e.getMessage());
        }
        return "redirect:/dashboard?tab=salaries";
    }

    /**
     * Performs a bulk deletion of multiple payment records.
     * <p>
     * This method iterates through a list of provided IDs and deletes the corresponding
     * payments. It handles cases where no IDs are provided and catches exceptions during
     * the deletion process.
     * </p>
     *
     * @param ids                a list of unique identifiers for the payments to be deleted
     * @param redirectAttributes used to pass success or error messages to the redirected view
     * @return a redirect string to the salaries tab of the dashboard
     */
    @PostMapping("/bulk-delete")
    public String bulkDelete(@RequestParam(value = "ids", required = false) List<Long> ids, RedirectAttributes redirectAttributes) {
        if (ids == null || ids.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie wybrano żadnych pozycji do usunięcia.");
            return "redirect:/dashboard?tab=salaries";
        }

        try {
            for (Long id : ids) {
                paymentRepository.deletePayment(id);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Usunięto " + ids.size() + " pozycji.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd masowego usuwania: " + e.getMessage());
        }
        return "redirect:/dashboard?tab=salaries";
    }

    /**
     * Updates the payment status for multiple records simultaneously.
     * <p>
     * This method accepts a Data Transfer Object containing a list of payment IDs and
     * the target status ID. It updates each specified payment record to the new status.
     * </p>
     *
     * @param bulkDto            the DTO containing the list of payment IDs and the new status ID
     * @param redirectAttributes used to pass success or error messages to the redirected view
     * @return a redirect string to the salaries tab of the dashboard
     */
    @PostMapping("/bulk-status")
    public String bulkUpdateStatus(@ModelAttribute BulkStatusUpdateDto bulkDto, RedirectAttributes redirectAttributes) {
        if (bulkDto.getIds() == null || bulkDto.getIds().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie wybrano żadnych pozycji.");
            return "redirect:/dashboard?tab=salaries";
        }

        try {
            for (Long id : bulkDto.getIds()) {
                paymentRepository.updateStatus(id, bulkDto.getStatusId());
            }
            redirectAttributes.addFlashAttribute("successMessage", "Zaktualizowano statusy dla " + bulkDto.getIds().size() + " wypłat.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd aktualizacji statusów: " + e.getMessage());
        }
        return "redirect:/dashboard?tab=salaries";
    }
}