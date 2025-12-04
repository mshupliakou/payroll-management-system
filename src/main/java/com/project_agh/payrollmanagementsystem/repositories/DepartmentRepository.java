package com.project_agh.payrollmanagementsystem.repositories;
import com.project_agh.payrollmanagementsystem.entities.Department;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepartmentRepository{
    List<Department> findAll();

    void createDepartment(String department_name, String department_desc);
}

