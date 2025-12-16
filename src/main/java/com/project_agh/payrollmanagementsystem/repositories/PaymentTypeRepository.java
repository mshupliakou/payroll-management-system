package com.project_agh.payrollmanagementsystem.repositories;

import com.project_agh.payrollmanagementsystem.entities.PaymentType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTypeRepository {
    List<PaymentType> findAll();

    void createPaymentType(String name, String description );

    void deletePaymentType(Long id);

    void editPaymentType(Long id, String name, String description);
}
