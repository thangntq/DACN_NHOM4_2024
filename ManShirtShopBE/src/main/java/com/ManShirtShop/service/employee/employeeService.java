package com.ManShirtShop.service.employee;

import java.util.List;

import com.ManShirtShop.dto.employee.employeeRequest;
import com.ManShirtShop.dto.employee.employeeResponse;

public interface employeeService {
    List<employeeResponse> getAll();

    employeeResponse Create(employeeRequest request);

    employeeResponse update(employeeRequest request);

    employeeResponse delete(Integer id);

    employeeResponse findById(Integer id);
}
