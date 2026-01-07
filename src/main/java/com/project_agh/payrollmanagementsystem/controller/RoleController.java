package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.RoleDto;
import com.project_agh.payrollmanagementsystem.repositories.RoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for managing user roles within the system.
 * <p>
 * This controller provides functionality for creating, deleting, and modifying roles
 * (e.g., ADMIN, USER, ACCOUNTANT). It ensures that role names are standardized (uppercase)
 * and restricts access to users with the {@code ROLE_ADMIN} authority.
 * </p>
 */
@Controller
@RequestMapping("admin/roles")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RoleController {

    private final RoleRepository roleRepository;

    /**
     * Constructs a new {@code RoleController} with the required dependency.
     *
     * @param roleRepository the repository used for role persistence operations
     */
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Handles the creation of a new user role.
     * <p>
     * Takes form data from the {@link RoleDto}, converts the role name to uppercase
     * to maintain system consistency, and persists it via the repository.
     * </p>
     *
     * @param roleDto            the DTO containing the name of the new role
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @param request            the current HTTP request
     * @return a redirect string to the roles tab of the dashboard
     */
    @PostMapping("/create")
    public String createRole(
            @ModelAttribute RoleDto roleDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            roleRepository.createRole(
                    roleDto.getName().toUpperCase()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Role has been created successfully."
            );

        } catch (Exception e) {
            System.err.println("Error creating role: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to create role. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=roles";
    }

    /**
     * Handles the deletion of an existing role.
     * <p>
     * Deletes the role identified by the ID within the provided DTO.
     * </p>
     *
     * @param roleDto            the DTO containing the ID of the role to delete
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @param request            the current HTTP request
     * @return a redirect string to the roles tab of the dashboard
     */
    @PostMapping("/delete")
    public String deleteRole(
            @ModelAttribute RoleDto roleDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            roleRepository.deleteRole(
                    roleDto.getId()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Role has been deleted successfully."
            );

        } catch (Exception e) {
            System.err.println("Error deleting role: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to delete role. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=roles";
    }

    /**
     * Handles the modification of an existing role's name.
     * <p>
     * Updates the name of the role identified by the provided ID. The new name is
     * automatically converted to uppercase.
     * </p>
     *
     * @param id                 the unique identifier of the role to edit
     * @param name               the new name for the role
     * @param redirectAttributes used to supply success or error messages to the view after redirection
     * @return a redirect string to the roles tab of the dashboard
     */
    @PostMapping("/edit")
    public String editRole(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            RedirectAttributes redirectAttributes) {

        try {
            roleRepository.editRole(
                    id, name.toUpperCase()
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "Dane roli (ID: " + id + ") zostały zaktualizowane.");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error editing role: " + e.getMessage());
        }

        return "redirect:/dashboard?tab=roles";
    }
}