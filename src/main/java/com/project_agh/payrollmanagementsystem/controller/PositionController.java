package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.CreateDepartmentDto;
import com.project_agh.payrollmanagementsystem.dtos.PositionDto;
import com.project_agh.payrollmanagementsystem.repositories.DepartmentRepository;
import com.project_agh.payrollmanagementsystem.repositories.PositionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for managing department-related administrative operations.
 * <p>
 * This controller provides functionality available only to users with the {@code ROLE_ADMIN} role.
 * It handles department creation via a POST request and delegates persistence logic to the
 * {@link DepartmentRepository}.
 */
@Controller
@RequestMapping("admin/positions")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PositionController {

    private final PositionRepository positionRepository;
    public PositionController(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

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

        return "redirect:/dashboard";
    }
}
