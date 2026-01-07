package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.CreateDepartmentDto;
import com.project_agh.payrollmanagementsystem.repositories.DepartmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for managing department-related administrative operations.
 * <p>
 * This controller provides functionality available only to users with the {@code ROLE_ADMIN} role.
 * It handles department creation, deletion, and modification via POST requests and delegates
 * persistence logic to the {@link DepartmentRepository}.
 * </p>
 */
@Controller
@RequestMapping("admin/departments")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DepartmentManagementController {

    /**
     * Repository used for executing department-related database operations.
     */
    private final DepartmentRepository departmentRepository;

    /**
     * Constructs a new {@code DepartmentManagementController} with the required dependencies.
     *
     * @param departmentRepository the repository used for department persistence operations
     */
    public DepartmentManagementController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * Handles the creation of a new department.
     * <p>
     * The method receives department data through a {@link CreateDepartmentDto} object,
     * invokes the repository layer to persist the department, and then redirects the user
     * back to the dashboard. Success or error messages are passed using flash attributes.
     * </p>
     *
     * @param newDepartmentForm   the DTO containing submitted department details
     * @param redirectAttributes  used to supply success or error messages after redirect
     * @param request             the current HTTP request (automatically injected by Spring)
     * @return redirect instruction to the dashboard view
     */
    @PostMapping("/create")
    public String createDepartment(
            @ModelAttribute CreateDepartmentDto newDepartmentForm,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            departmentRepository.createDepartment(
                    newDepartmentForm.getDepartment_name(),
                    newDepartmentForm.getDepartment_desc()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Department has been created successfully."
            );

        } catch (Exception e) {
            System.err.println("Error creating department: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to create department. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=departments";
    }

    /**
     * Handles the deletion of an existing department.
     * <p>
     * This method accepts a data transfer object containing the ID of the department to be deleted.
     * It performs the delete operation via the repository and sets a success or error message
     * to be displayed on the dashboard.
     * </p>
     *
     * @param departmentDto       the DTO containing the ID of the department to delete
     * @param redirectAttributes  used to supply success or error messages after redirect
     * @param request             the current HTTP request
     * @return redirect instruction to the dashboard view (departments tab)
     */
    @PostMapping("/delete")
    public String deleteDepartment(
            @ModelAttribute CreateDepartmentDto departmentDto,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            departmentRepository.deleteDepartment(
                    departmentDto.getId()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Department has been deleted successfully."
            );

        } catch (Exception e) {
            System.err.println("Error deleting role: " + e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error: Failed to delete role. Details: " + e.getMessage()
            );
        }

        return "redirect:/dashboard?tab=departments";
    }

    /**
     * Handles the modification of an existing department's details.
     * <p>
     * This method updates the name and description of a department identified by its ID.
     * The parameters are bound directly from the HTTP request parameters.
     * </p>
     *
     * @param id                  the unique identifier of the department to edit
     * @param name                the new name for the department
     * @param description         the new description for the department
     * @param redirectAttributes  used to supply success or error messages after redirect
     * @return redirect instruction to the dashboard view (departments tab)
     */
    @PostMapping("/edit")
    public String editDepartment(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            RedirectAttributes redirectAttributes) {

        try {
            departmentRepository.editDepartment(
                    id, name, description
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "Dane działa (ID: " + id + ") zostały zaktualizowane.");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error editing department: " + e.getMessage());
        }

        return "redirect:/dashboard?tab=departments";
    }
}