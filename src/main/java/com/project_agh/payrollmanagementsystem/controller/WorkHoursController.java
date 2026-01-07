package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.WorkHoursDto;
import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.repositories.UserRepository;
import com.project_agh.payrollmanagementsystem.repositories.WorkHoursRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for managing employee work hours.
 * <p>
 * This controller handles the lifecycle of work hour entries, including creation,
 * modification, deletion, and administrative approval. It interacts with the security
 * context to identify the currently logged-in user when creating new records.
 * </p>
 */
@Controller
public class WorkHoursController {

    private final WorkHoursRepository workHoursRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new {@code WorkHoursController} with the required dependencies.
     *
     * @param workHoursRepository repository for work hours persistence operations
     * @param userRepository      repository for user data access (used to fetch the current user)
     */
    public WorkHoursController(WorkHoursRepository workHoursRepository, UserRepository userRepository) {
        this.workHoursRepository = workHoursRepository;
        this.userRepository = userRepository;
    }

    /**
     * Handles the creation of a new work hour entry.
     * <p>
     * This method retrieves the currently authenticated user from the security context to ensure
     * the record is assigned to the correct person. It supports creating entries both linked
     * to a specific project and those that are not.
     * </p>
     *
     * @param workHoursDto       the DTO containing the date, time, project, and work type details
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @return a redirect string to the work hours tab of the dashboard
     */
    @PostMapping("/work_hours/create")
    public String createWorkHours(
            @ModelAttribute WorkHoursDto workHoursDto,
            RedirectAttributes redirectAttributes) {

        try {
            // 1. Retrieve the currently authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 2. Check if a project was selected
            Long projectId = workHoursDto.getProjectId();

            // Logic: Use project-specific method if project ID exists, otherwise use the standard method.
            if (projectId != null) {
                workHoursRepository.createWorkHoursWithProject(
                        currentUser.getId(), // Use ID from logged-in user, not DTO
                        workHoursDto.getDate(),
                        workHoursDto.getWorkTypeId(),
                        projectId,
                        workHoursDto.getStartTime(),
                        workHoursDto.getEndTime(),
                        workHoursDto.getComment()
                );
            } else {
                workHoursRepository.createWorkHours(
                        currentUser.getId(),
                        workHoursDto.getDate(),
                        workHoursDto.getWorkTypeId(),
                        workHoursDto.getStartTime(),
                        workHoursDto.getEndTime(),
                        workHoursDto.getComment()
                );
            }

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Godziny pracy zostały dodane pomyślnie."
            );

        } catch (Exception e) {
            e.printStackTrace(); // Log error to console
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Błąd: Nie udało się dodać godzin. Szczegóły: " + e.getMessage()
            );
        }

        // Return to the work hours tab
        return "redirect:/dashboard?tab=work_hours";
    }

    /**
     * Handles the modification of an existing work hour entry.
     * <p>
     * Updates details such as the date, work type, start/end times, and comments for
     * a specific record identified by its ID.
     * </p>
     *
     * @param workHoursDto       the DTO containing the updated work hour details
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @return a redirect string to the work hours tab of the dashboard
     */
    @PostMapping("/work_hours/edit")
    public String editWorkHours(
            @ModelAttribute WorkHoursDto workHoursDto,
            RedirectAttributes redirectAttributes) {

        try {
            workHoursRepository.editWorkHours(
                    workHoursDto.getId(),
                    workHoursDto.getUserId(),
                    workHoursDto.getDate(),
                    workHoursDto.getWorkTypeId(),
                    workHoursDto.getStartTime(),
                    workHoursDto.getEndTime(),
                    workHoursDto.getComment()
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "Dane  (ID: " + workHoursDto.getId() + ") zostały zaktualizowane.");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error editing work type: " + e.getMessage());
        }

        return "redirect:/dashboard?tab=work_hours";
    }

    /**
     * Handles the deletion of a work hour entry.
     * <p>
     * Permanently removes the record identified by the ID provided in the DTO.
     * </p>
     *
     * @param workHoursDto       the DTO containing the ID of the work hour entry to delete
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @return a redirect string to the work hours tab of the dashboard
     */
    @PostMapping("/work_hours/delete")
    public String deleteWorkHours(
            @ModelAttribute WorkHoursDto workHoursDto,
            RedirectAttributes redirectAttributes) {
        try {
            workHoursRepository.deleteWorkHours(workHoursDto.getId());

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Work hour has been deleted successfully."
            );

        } catch (Exception e) {
            System.err.println("Error deleting work hour: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to delete work hour. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=work_hours";
    }

    /**
     * Approves a specific work hour entry.
     * <p>
     * This endpoint is typically accessible only to administrators or managers.
     * It marks the work hour record as approved, allowing it to be processed for payroll.
     * </p>
     *
     * @param workHoursDto       the DTO containing the ID of the work hour entry to approve
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @return a redirect string to the approvals tab of the dashboard
     */
    @PostMapping("admin/work_hours/approve")
    public String approveWorkHours(
            @ModelAttribute WorkHoursDto workHoursDto,
            RedirectAttributes redirectAttributes) {
        try {
            workHoursRepository.approveWorkHours(workHoursDto.getId());

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Work hour has been approved successfully."
            );

        } catch (Exception e) {
            System.err.println("Error approving work hour: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to approve work hour. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=approvals";
    }
}