package com.ManShirtShop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import com.ManShirtShop.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    @Query(value = "SELECT * FROM employee WHERE employee.status = 0 ORDER BY employee.create_time DESC", nativeQuery = true)
    List<Employee> getAllByStatus();

    Optional<Employee> findByEmail(String email);

    @Query(value = "SELECT employee.fullname FROM employee WHERE employee.email = :email", nativeQuery = true)
    String  getFullNameByEmail(@Param("email") String email);
 
}
