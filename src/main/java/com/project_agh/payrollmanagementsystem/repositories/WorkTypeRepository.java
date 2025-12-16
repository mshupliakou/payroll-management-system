package com.project_agh.payrollmanagementsystem.repositories;
import com.project_agh.payrollmanagementsystem.entities.WorkType;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkTypeRepository {
    List<WorkType> findAll();
    void createWorkType(String name, String description);
    void deleteWorkType(Long id);
    void editWorkType(Long id, String name, String description);
}
