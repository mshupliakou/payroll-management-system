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
            PaymentStatusRepository paymentStatusRepository, SalaryChangeHistoryRepository salaryChangeHistoryRepository, PaymentRepository paymentRepository
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

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @RequestParam(value = "weekOffset", defaultValue = "0") int weekOffset,
                            @RequestParam(value = "tab", required = false) String tab) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }

        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found: " + userEmail));

        model.addAttribute("user", user);

        model.addAttribute("passwordChangeForm", new PasswordChangeDto());
        model.addAttribute("phoneNumberChangeForm", new PhoneNumberDto());
        model.addAttribute("newUserForm", new CreateUserDto());
        model.addAttribute("AccountChangeForm", new BankAccountDto());

        LocalDate today = LocalDate.now();
        LocalDate selectedDate = today.plusWeeks(weekOffset);
        LocalDate startOfWeek = selectedDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = selectedDate.with(java.time.DayOfWeek.SUNDAY);

        model.addAttribute("currentWeekOffset", weekOffset);
        model.addAttribute("weekStart", startOfWeek);
        model.addAttribute("weekEnd", endOfWeek);


        List<WorkHours> statsRecords = workHoursRepository.findByUserIdAndDateRange(user.getId(), startOfWeek, endOfWeek);
        List<WorkHours> allHistoryRecords = workHoursRepository.findByUserId(user.getId());

        model.addAttribute("myWorkHoursList", allHistoryRecords);


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

        EmployeeStats globalStats = employeeStatsRepository.findById(user.getId())
                .orElse(new EmployeeStats(user.getId(), 0.0, 0, 0.0));

        model.addAttribute("sum_hours", String.format("%.2f h", globalStats.getTotalHours()));
        model.addAttribute("avareage_per_week", String.format("%.2f h", globalStats.getAverageWeeklyHours()));

        model.addAttribute("projectsList", projectRepository.findAll());
        model.addAttribute("workTypesList", workTypeRepository.findAll());
        model.addAttribute("newWorkHoursForm", new WorkHoursDto());
        model.addAttribute("newPaymentTypeForm", new PaymentTypeDto());
        model.addAttribute("allUsers", Collections.emptyList());
        model.addAttribute("notApprovedUsers", Collections.emptyList());
        model.addAttribute("newPaymentStatusForm", new PaymentStatusDto());

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