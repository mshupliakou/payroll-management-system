package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.WorkTypeDto;
import com.project_agh.payrollmanagementsystem.repositories.WorkTypeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for managing different types of work available in the system
 * (e.g., Remote, Office, Business Trip).
 * <p>
 * This controller provides administrative functionality to create, edit, and delete work types.
 * Access is restricted to users with the {@code ROLE_ADMIN} authority.
 * All operations are mapped under the {@code admin/work_types} endpoint.
 * </p>
 */
@Controller
@RequestMapping("admin/work_types")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class WorkTypeController {

    private final WorkTypeRepository workTypeRepository;

    /**
     * Constructs a new {@code WorkTypeController} with the required dependency.
     *
     * @param workTypeRepository the repository used for work type persistence operations
     */
    public WorkTypeController(WorkTypeRepository workTypeRepository) {
        this.workTypeRepository = workTypeRepository;
    }

    /**
     * Handles the creation of a new work type.
     * <p>
     * Accepts form data via {@link WorkTypeDto}, persists the new work type using the repository,
     * and redirects the user back to the work types dashboard tab.
     * </p>
     *
     * @param workTypeDto        the DTO containing the name and description of the new work type
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @param request            the current HTTP request
     * @return a redirect string to the work types tab of the dashboard
     */
    @PostMapping("/create")
    public String createWorkType(
            @ModelAttribute WorkTypeDto workTypeDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            workTypeRepository.createWorkType(
                    workTypeDto.getName(),
                    workTypeDto.getDescription()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "WorkType has been created successfully."
            );

        } catch (Exception e) {
            System.err.println("Error creating work type: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to create work type. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=work_types";
    }

    /**
     * Handles the deletion of an existing work type.
     * <p>
     * Deletes the work type identified by the ID within the provided DTO.
     * </p>
     *
     * @param workTypeDto        the DTO containing the ID of the work type to delete
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @param request            the current HTTP request
     * @return a redirect string to the work types tab of the dashboard
     */
    @PostMapping("/delete")
    public String deleteWorktype(
            @ModelAttribute WorkTypeDto workTypeDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            workTypeRepository.deleteWorkType(workTypeDto.getId());

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Work type has been deleted successfully."
            );

        } catch (Exception e) {
            System.err.println("Error deleting work type: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to delete work type. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=work_types";
    }

    /**
     * Handles the modification of an existing work type.
     * <p>
     * Updates the name and description of the work type identified by the provided ID.
     * </p>
     *
     * @param workTypeDto        the DTO containing the updated details (ID, name, description)
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @return a redirect string to the work types tab of the dashboard
     */
    @PostMapping("/edit")
    public String editWorkType(
            @ModelAttribute WorkTypeDto workTypeDto,
            RedirectAttributes redirectAttributes) {

        try {
            workTypeRepository.editWorkType(
                    workTypeDto.getId(),
                    workTypeDto.getName(),
                    workTypeDto.getDescription()
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "Dane typa pracy (ID: " + workTypeDto.getId() + ") zostały zaktualizowane.");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error editing work type: " + e.getMessage());
        }

        return "redirect:/dashboard?tab=work_types";
    }
}