
package com.company.employee.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.company.employee.entity.Employee;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long>,
                JpaSpecificationExecutor<Employee> {

    // Find employee by email
    Optional<Employee> findByEmail(String email);

    // Check duplicate email
    boolean existsByEmail(String email);

    // Search employees by name
    List<Employee> findByNameContainingIgnoreCase(String name);

    // Find employees by department
    List<Employee> findByDepartment_Id(Long departmentId);

    // Find employees with salary greater than given amount
    List<Employee> findBySalaryGreaterThan(BigDecimal salary);
}
