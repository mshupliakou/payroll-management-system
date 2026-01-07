package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.ProjectDto;
import com.project_agh.payrollmanagementsystem.repositories.ProjectRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * Controller responsible for managing projects within the system.
 * <p>
 * This controller allows administrators to create, edit, delete, and manage user assignments
 * for projects. It is restricted to users with the {@code ROLE_ADMIN} authority.
 * All endpoints are mapped under {@code admin/projects}.
 * </p>
 */
@Controller
@RequestMapping("admin/projects")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ProjectController {

    private final ProjectRepository projectRepository;

    /**
     * Constructs a new {@code ProjectController} with the required dependency.
     *
     * @param projectRepository the repository used for project persistence operations
     */
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Creates a new project in the system.
     * <p>
     * Accepts project details via {@link ProjectDto}, persists the new project,
     * and redirects the user to the projects dashboard tab.
     * </p>
     *
     * @param projectDto         the DTO containing the name, description, and date range of the project
     * @param redirectAttributes used to supply success or error messages to the view
     * @param request            the current HTTP request
     * @return a redirect string to the projects tab of the dashboard
     */
    @PostMapping("/create")
    public String createProject(
            @ModelAttribute ProjectDto projectDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            projectRepository.createProject(
                    projectDto.getName(),
                    projectDto.getDescription(),
                    projectDto.getProjectBeginDate(),
                    projectDto.getProjectEndDate()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Project has been created successfully."
            );

        } catch (Exception e) {
            System.err.println("Error creating project: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to create project. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=projects";
    }

    /**
     * Deletes an existing project.
     * <p>
     * Removes the project identified by the ID in the provided DTO from the database.
     * </p>
     *
     * @param projectDto         the DTO containing the ID of the project to delete
     * @param redirectAttributes used to supply success or error messages to the view
     * @param request            the current HTTP request
     * @return a redirect string to the projects tab of the dashboard
     */
    @PostMapping("/delete")
    public String deleteProject(
            @ModelAttribute ProjectDto projectDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            projectRepository.deleteProject(projectDto.getId());

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Project has been deleted successfully."
            );

        } catch (Exception e) {
            System.err.println("Error deleting project: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to delete role. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=projects";
    }

    /**
     * Updates the details of an existing project.
     * <p>
     * Modifies the name, description, and date range of a project identified by its ID.
     * </p>
     *
     * @param projectDto         the DTO containing the updated project details
     * @param redirectAttributes used to supply success or error messages to the view
     * @return a redirect string to the projects tab of the dashboard
     */
    @PostMapping("/edit")
    public String editProject(
            @ModelAttribute ProjectDto projectDto,
            RedirectAttributes redirectAttributes) {

        try {
            projectRepository.editProject(
                    projectDto.getId(),
                    projectDto.getName(),
                    projectDto.getDescription(),
                    projectDto.getProjectBeginDate(),
                    projectDto.getProjectEndDate()
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "Dane projekta (ID: " + projectDto.getId() + ") zostały zaktualizowane.");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error editing project: " + e.getMessage());
        }

        return "redirect:/dashboard?tab=projects";
    }

    /**
     * Assigns a user to a specific project.
     * <p>
     * Links a user to a project with a specific role. The assignment date is set to the current date.
     * </p>
     *
     * @param projectId          the ID of the project
     * @param userId             the ID of the user to assign
     * @param role               the role the user will fulfill in the project
     * @param redirectAttributes used to supply success or error messages to the view
     * @return a redirect string to the projects tab of the dashboard
     */
    @PostMapping("/add_user")
    public String addUserToProject(@RequestParam("projectId") Long projectId,
                                   @RequestParam("userId") Long userId,
                                   @RequestParam("role") String role,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Invoking the repository method to link user to project
            projectRepository.addUserToProject(projectId, userId, role, LocalDate.now());
            redirectAttributes.addFlashAttribute("successMessage", "Użytkownik dodany do projektu.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: " + e.getMessage());
        }
        return "redirect:/dashboard?tab=projects";
    }

    /**
     * Removes a user from a specific project.
     * <p>
     * Deletes the association between the specified user and project.
     * </p>
     *
     * @param projectId          the ID of the project
     * @param userId             the ID of the user to remove
     * @param redirectAttributes used to supply success or error messages to the view
     * @return a redirect string to the projects tab of the dashboard
     */
    @PostMapping("/remove_user")
    public String addUserToProject(@RequestParam("projectId") Long projectId,
                                   @RequestParam("userId") Long userId,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Invoking the repository method to remove user from project
            projectRepository.removeUserFromProject(projectId, userId);
            redirectAttributes.addFlashAttribute("successMessage", "Użytkownik usunięty z projektu.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: " + e.getMessage());
        }
        return "redirect:/dashboard?tab=projects";
    }

}