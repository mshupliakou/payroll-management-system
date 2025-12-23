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

@Controller
@RequestMapping("/accountant/payments") // Общий префикс для всех методов
public class PaymentController {

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

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