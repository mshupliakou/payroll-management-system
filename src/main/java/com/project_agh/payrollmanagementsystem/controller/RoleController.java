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

@Controller
@RequestMapping("admin/roles")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RoleController {

    private final RoleRepository roleRepository;
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

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