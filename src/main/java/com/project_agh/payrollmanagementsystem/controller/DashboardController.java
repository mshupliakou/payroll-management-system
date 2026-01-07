package com.project_agh.payrollmanagementsystem.controller;

import com.project_agh.payrollmanagementsystem.dtos.*;
import com.project_agh.payrollmanagementsystem.entities.EmployeeStats;
import com.project_agh.payrollmanagementsystem.entities.User;
import com.project_agh.payrollmanagementsystem.entities.WorkHours;
import com.project_agh.payrollmanagementsystem.repositories.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Main controller responsible for rendering the application dashboard.
 * <p>
 * This controller aggregates data from various repositories to populate the model
 * with necessary information for different user roles (USER, ADMIN, ACCOUNTANT).
 * It handles logic for displaying statistics, forms, and management lists.
 * </p>
 */
@Controller
public class DashboardController {

    private final WorkTypeRepository workTypeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final ProjectRepository projectRepository;
    private final WorkHoursRepository workHoursRepository;
    private final EmployeeStatsRepository employeeStatsRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final SalaryChangeHistoryRepository salaryChangeHistoryRepository;
    private final PaymentRepository paymentRepository;

    /**
     * Constructs the DashboardController with all necessary repository dependencies.
     * These dependencies are injected by Spring Framework.
     *
     * @param userRepository                Repository for User entity operations.
     * @param roleRepository                Repository for Role entity operations.
     * @param departmentRepository          Repository for Department entity operations.
     * @param positionRepository            Repository for Position entity operations.
     * @param projectRepository             Repository for Project entity operations.
     * @param workTypeRepository            Repository for WorkType entity operations.
     * @param workHoursRepository           Repository for WorkHours entity operations.
     * @param employeeStatsRepository       Repository for EmployeeStats view/entity.
     * @param paymentTypeRepository         Repository for PaymentType entity operations.
     * @param paymentStatusRepository       Repository for PaymentStatus entity operations.
     * @param salaryChangeHistoryRepository Repository for SalaryChangeHistory entity operations.
     * @param paymentRepository             Repository for Payment entity operations.
     */
    public DashboardController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            DepartmentRepository departmentRepository,
            PositionRepository positionRepository,
            ProjectRepository projectRepository,
            WorkTypeRepository workTypeRepository,
            WorkHoursRepository workHoursRepository,
            EmployeeStatsRepository employeeStatsRepository,
            PaymentTypeRepository paymentTypeRepository,
            PaymentStatusRepository paymentStatusRepository,
            SalaryChangeHistoryRepository salaryChangeHistoryRepository,
            PaymentRepository paymentRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.projectRepository = projectRepository;
        this.workTypeRepository= workTypeRepository;
        this.workHoursRepository = workHoursRepository;
        this.employeeStatsRepository = employeeStatsRepository;
        this.paymentTypeRepository = paymentTypeRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.salaryChangeHistoryRepository = salaryChangeHistoryRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Handles GET requests to the "/dashboard" endpoint.
     * <p>
     * This method prepares the data model for the dashboard view. It performs the following steps:
     * <ul>
     * <li>Authenticates the user and redirects to login if necessary.</li>
     * <li>Loads common data forms (Password change, User info, etc.).</li>
     * <li>Calculates statistics for the current user based on the selected week offset.</li>
     * <li>Loads specific data based on the user's role (ADMIN or ACCOUNTANT).</li>
     * </ul>
     *
     * @param model      The Spring UI Model used to pass attributes to the view.
     * @param weekOffset An optional integer indicating the offset from the current week (default is 0).
     * Used for pagination of weekly statistics.
     * @param tab        An optional string indicating which tab should be active on load.
     * @return The name of the Thymeleaf template to render ("dashboard") or a redirect string.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @RequestParam(value = "weekOffset", defaultValue = "0") int weekOffset,
                            @RequestParam(value = "tab", required = false) String tab) {

        // Check authentication status
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }

        // Retrieve authenticated user
        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found: " + userEmail));

        model.addAttribute("user", user);

        // Add common forms to the model
        model.addAttribute("passwordChangeForm", new PasswordChangeDto());
        model.addAttribute("phoneNumberChangeForm", new PhoneNumberDto());
        model.addAttribute("newUserForm", new CreateUserDto());
        model.addAttribute("AccountChangeForm", new BankAccountDto());

        // Calculate date range for the selected week
        LocalDate today = LocalDate.now();
        LocalDate selectedDate = today.plusWeeks(weekOffset);
        LocalDate startOfWeek = selectedDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = selectedDate.with(java.time.DayOfWeek.SUNDAY);

        model.addAttribute("currentWeekOffset", weekOffset);
        model.addAttribute("weekStart", startOfWeek);
        model.addAttribute("weekEnd", endOfWeek);

        // Fetch work hours statistics
        List<WorkHours> statsRecords = workHoursRepository.findByUserIdAndDateRange(user.getId(), startOfWeek, endOfWeek);
        List<WorkHours> allHistoryRecords = workHoursRepository.findByUserId(user.getId());

        model.addAttribute("myWorkHoursList", allHistoryRecords);

        // Calculate weekly metrics
        double currentWeekMinutes = 0;
        long daysWorkedCount = statsRecords.stream()
                .map(WorkHours::getDate)
                .distinct()
                .count();

        for (WorkHours record : statsRecords) {
            if (record.getStartTime() != null && record.getEndTime() != null) {
                currentWeekMinutes += java.time.Duration.between(record.getStartTime(), record.getEndTime()).toMinutes();
            }
        }

        double currentWeekHours = currentWeekMinutes / 60.0;
        double dailyAverage = (daysWorkedCount == 0) ? 0.0 : currentWeekHours / daysWorkedCount;

        model.addAttribute("sum_per_week", String.format("%.2f h", currentWeekHours));
        model.addAttribute("avarage_per_day", String.format("%.2f h", dailyAverage));

        // Fetch global employee stats
        EmployeeStats globalStats = employeeStatsRepository.findById(user.getId())
                .orElse(new EmployeeStats(user.getId(), 0.0, 0, 0.0));

        model.addAttribute("sum_hours", String.format("%.2f h", globalStats.getTotalHours()));
        model.addAttribute("avareage_per_week", String.format("%.2f h", globalStats.getAverageWeeklyHours()));

        // Add standard lists and forms
        model.addAttribute("projectsList", projectRepository.findAll());
        model.addAttribute("workTypesList", workTypeRepository.findAll());
        model.addAttribute("newWorkHoursForm", new WorkHoursDto());
        model.addAttribute("newPaymentTypeForm", new PaymentTypeDto());
        model.addAttribute("allUsers", Collections.emptyList());
        model.addAttribute("notApprovedUsers", Collections.emptyList());
        model.addAttribute("newPaymentStatusForm", new PaymentStatusDto());

        // Logic specifically for ADMINISTRATORS
        if (user.getRole().getName().equals("ADMIN")) {
            model.addAttribute("newUserInProjectForm", new ProjectUserDto());
            model.addAttribute("rolesList", roleRepository.findAll());
            model.addAttribute("positionsList", positionRepository.findAll());
            model.addAttribute("departmentsList", departmentRepository.findAll());
            model.addAttribute("allUsers", userRepository.findAll());
            model.addAttribute("notApprovedUsers", userRepository.findAllNotApproved());
            model.addAttribute("newDepartmentForm", new CreateDepartmentDto());
            model.addAttribute("newPositionForm", new PositionDto());
            model.addAttribute("newRoleForm", new RoleDto());
            model.addAttribute("newProjectForm", new ProjectDto());
            model.addAttribute("newWorkTypeForm", new WorkTypeDto());
        }

        // Logic specifically for ACCOUNTANTS
        if (user.getRole().getName().equals("ACCOUNTANT")) {
            model.addAttribute("allUsers", userRepository.findAll());
            List<WorkHours> allUsersRecords = workHoursRepository.findAll();
            model.addAttribute("WorkHoursList", allUsersRecords);
            model.addAttribute("newPaymentStatusForm", new PaymentStatusDto());
            model.addAttribute("paymentTypesList", paymentTypeRepository.findAll());
            model.addAttribute("paymentStatusesList", paymentStatusRepository.findAll());
            model.addAttribute("salaryChangeHistoryList", salaryChangeHistoryRepository.findAll());
            model.addAttribute("paymentStatusesList", paymentStatusRepository.findAll());
            model.addAttribute("paymentHistoryList", paymentRepository.findAll());
        }

        return "dashboard";
    }
}