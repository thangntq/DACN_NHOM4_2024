package com.ManShirtShop.service.employee.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.employee.employeeRequest;
import com.ManShirtShop.dto.employee.employeeResponse;
import com.ManShirtShop.entities.Employee;
import com.ManShirtShop.entities.Role;
import com.ManShirtShop.repository.EmployeeRepository;
import com.ManShirtShop.repository.RoleRepository;
import com.ManShirtShop.service.employee.employeeService;

@Service
public class employeeServiceImpl implements employeeService{

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public Boolean checkId(Integer id, Boolean checkDB) {
        if (id <= 0 || id == null) {
            return true;
        }
        if (!checkDB) { // check id db
            return true;
        }
        return false;
    }
    public Employee checkAndReturnProductDetail(employeeRequest request) {
        if (checkId(request.getRole(), roleRepository.existsById(request.getRole()))) {
            return null;
        }
        Employee entity = ObjectMapperUtils.map(request, Employee.class);

        Role role = new Role();
        role.setId(request.getRole());
        entity.setRole(role);

        return entity;
    }
    @Override
    public List<employeeResponse> getAll() {
        List<Employee> getAll = employeeRepository.getAllByStatus();
        return ObjectMapperUtils.mapAll(getAll, employeeResponse.class);
    }

    @Override
    public employeeResponse Create(employeeRequest request) {
        request.setId(0);
        Employee entity = checkAndReturnProductDetail(request);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = employeeRepository.save(entity);
        return ObjectMapperUtils.map(entity, employeeResponse.class);
    }

    @Override
    public employeeResponse update(employeeRequest request) {
        if (checkId(request.getId(), employeeRepository.existsById(request.getId()))) {
            return null;
        }
        Employee entityDB = employeeRepository.findById(request.getId()).get();
        Employee entity = checkAndReturnProductDetail(request);
        entity.setCreateBy(entityDB.getCreateBy());
        entity.setCreateTime(entityDB.getCreateTime());
        entity.setUpdateBy("amdin");
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = employeeRepository.save(entity);
        return ObjectMapperUtils.map(entity, employeeResponse.class);
    }

    @Override
    public employeeResponse delete(Integer id) {
        if (checkId(id, employeeRepository.existsById(id))) {
            return null;
        }
        Employee entity = employeeRepository.findById(id).get();
        entity.setStatus(1);
        entity = employeeRepository.save(entity);
        return ObjectMapperUtils.map(entity, employeeResponse.class);
    }

    @Override
    public employeeResponse findById(Integer id) {
        if (checkId(id, employeeRepository.existsById(id))) {
            return null;
        }
        Employee entity = employeeRepository.findById(id).get();
        return ObjectMapperUtils.map(entity, employeeResponse.class);
    }
    
}
