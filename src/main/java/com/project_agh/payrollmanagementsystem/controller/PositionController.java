package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.PositionDto;
import com.project_agh.payrollmanagementsystem.repositories.PositionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for managing job positions within the organization.
 * <p>
 * This controller provides functionality available only to users with the {@code ROLE_ADMIN} role.
 * It handles the creation, deletion, and modification of positions via POST requests and delegates
 * persistence logic to the {@link PositionRepository}.
 * </p>
 */
@Controller
@RequestMapping("admin/positions")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PositionController {

    private final PositionRepository positionRepository;

    /**
     * Constructs a new {@code PositionController} with the required dependency.
     *
     * @param positionRepository the repository used for position persistence operations
     */
    public PositionController(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    /**
     * Handles the creation of a new job position.
     * <p>
     * This method accepts form data bound to a {@link PositionDto}, invokes the
     * repository to create the record, and redirects the user back to the dashboard.
     * </p>
     *
     * @param positionDto        the DTO containing the name and description of the new position
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @param request            the current HTTP request
     * @return a redirect string to the positions tab of the dashboard
     */
    @PostMapping("/create")
    public String createPosition(
            @ModelAttribute PositionDto positionDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            positionRepository.createPosition(
                    positionDto.getPosition_name(),
                    positionDto.getPosition_desc()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Position has been created successfully."
            );

        } catch (Exception e) {
            System.err.println("Error creating position: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to create position. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=positions";
    }

    /**
     * Handles the deletion of an existing job position.
     * <p>
     * This method expects a DTO containing the ID of the position to be deleted.
     * Upon success or failure, appropriate feedback messages are added to the redirect attributes.
     * </p>
     *
     * @param positionDto        the DTO containing the ID of the position to delete
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @param request            the current HTTP request
     * @return a redirect string to the positions tab of the dashboard
     */
    @PostMapping("/delete")
    public String deletePosition(
            @ModelAttribute PositionDto positionDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            positionRepository.deletePosition(
                    positionDto.getId()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Position has been deleted successfully."
            );

        } catch (Exception e) {
            System.err.println("Error deleting role: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to delete role. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=positions";
    }

    /**
     * Handles the modification of an existing position's details.
     * <p>
     * This method updates the name and description of a position identified by its ID.
     * Parameters are extracted directly from the request.
     * </p>
     *
     * @param id                 the unique identifier of the position to edit
     * @param name               the new name for the position
     * @param description        the new description for the position
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @return a redirect string to the positions tab of the dashboard
     */
    @PostMapping("/edit")
    public String editPosition(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            RedirectAttributes redirectAttributes) {

        try {
            positionRepository.editPosition(
                    id, name, description
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "Dane stanowiska (ID: " + id + ") zostały zaktualizowane.");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error editing position: " + e.getMessage());
        }

        return "redirect:/dashboard?tab=positions";
    }
}