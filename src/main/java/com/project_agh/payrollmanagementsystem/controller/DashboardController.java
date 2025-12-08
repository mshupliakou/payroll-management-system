package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.*;
import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.repositories.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

/**
 * Controller responsible for rendering the main dashboard page.
 * Displays user information and, for administrators, provides access
 * to management panels for roles, positions, departments and users.
 */
@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final ProjectRepository projectRepository;

    /**
     * Constructs a DashboardController with required repositories.
     *
     * @param userRepository Repository for user-related operations
     * @param roleRepository Repository for roles
     * @param departmentRepository Repository for departments
     * @param positionRepository Repository for positions
     */
    public DashboardController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            DepartmentRepository departmentRepository,
            PositionRepository positionRepository,
            ProjectRepository projectRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * Handles GET requests to "/dashboard".
     * <p>
     * The method performs the following steps:
     * <ul>
     *     <li>retrieves the authenticated user from Spring Security</li>
     *     <li>redirects to login if the user is not authenticated</li>
     *     <li>loads user details from the database</li>
     *     <li>adds required form objects and data collections to the model</li>
     *     <li>if the user is an administrator, loads additional admin data</li>
     * </ul>
     *
     * @param model the Model object used to pass attributes to the view
     * @return the name of the Thymeleaf template to render ("dashboard")
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // Retrieve authentication from SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Redirect to login if the user is anonymous or not authenticated
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }

        // The username equals the user's email
        String userEmail = auth.getName();

        // Fetch authenticated user's entity from DB
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found in DB: " + userEmail)
                );

        // Add core user data to the model
        model.addAttribute("user", user);

        // Provide default (empty) form objects to avoid Thymeleaf errors
        model.addAttribute("passwordChangeForm", new PasswordChangeDto());
        model.addAttribute("phoneNumberChangeForm", new PhoneNumberDto());
        model.addAttribute("newUserForm", new CreateUserDto());
        model.addAttribute("AccountChangeForm", new BankAccountDto());
        model.addAttribute("newWorkHoursForm", new WorkHoursDto());
        // Provide an empty list for non-admins (dashboard loops expect this attribute)
        model.addAttribute("allUsers", Collections.emptyList());

        // Load additional data if the current user is an administrator
        if (user.getRole().getName().equals("ADMIN")) {
            model.addAttribute("rolesList", roleRepository.findAll());
            model.addAttribute("positionsList", positionRepository.findAll());
            model.addAttribute("departmentsList", departmentRepository.findAll());
            model.addAttribute("projectsList", projectRepository.findAll());
            model.addAttribute("newUserForm", new CreateUserDto());
            model.addAttribute("allUsers", userRepository.findAll());
            model.addAttribute("newDepartmentForm", new CreateDepartmentDto());
            model.addAttribute("newPositionForm", new PositionDto());
            model.addAttribute("newRoleForm", new RoleDto());

            model.addAttribute("newProjectForm", new ProjectDto());
            model.addAttribute("newWorkTypeForm", new WorkTypeDto());


        }

        // Render the dashboard page
        return "dashboard";
    }
}
