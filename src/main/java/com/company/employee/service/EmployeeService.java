
package com.company.employee.service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee.dto.EmployeeRequest;
import com.company.employee.dto.EmployeeResponse;
import com.company.employee.entity.Department;
import com.company.employee.entity.Employee;
import com.company.employee.repository.DepartmentRepository;
import com.company.employee.repository.EmployeeRepository;
import com.company.employee.repository.EmployeeProjectRepository;
import com.company.employee.specification.EmployeeSpecification;
import com.company.employee.exception.ConflictException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeProjectRepository assignmentRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            EmployeeProjectRepository assignmentRepository) {

        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.assignmentRepository = assignmentRepository;
    }

    // CREATE EMPLOYEE
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmail(request.email())) {
            throw new ConflictException(
                "Employee email already exists"
            );
        }

        Department department = departmentRepository
            .findById(request.departmentId())
            .orElseThrow(() -> new NoSuchElementException(
                "Department not found"
            ));

        Employee employee = new Employee();

        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setSalary(request.salary());
        employee.setDesignation(request.designation());
        employee.setDepartment(department);

        Employee saved = employeeRepository.save(employee);

        return toResponse(saved);
    }

    // GET EMPLOYEE BY ID
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Employee not found with ID: " + id
            ));

        return toResponse(employee);
    }

    // GET ALL EMPLOYEES WITH PAGINATION
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {

        return employeeRepository.findAll(pageable)
            .map(this::toResponse);
    }

    // UPDATE EMPLOYEE
    @Transactional
    public EmployeeResponse updateEmployee(
            Long id, EmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Employee not found with ID: " + id
            ));

        if (!employee.getEmail().equalsIgnoreCase(request.email())
                && employeeRepository.existsByEmail(request.email())) {

            throw new ConflictException(
                "Email already belongs to another employee"
            );
        }

        Department department = departmentRepository
            .findById(request.departmentId())
            .orElseThrow(() -> new NoSuchElementException(
                "Department not found"
            ));

        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setSalary(request.salary());
        employee.setDesignation(request.designation());
        employee.setDepartment(department);

        Employee updated = employeeRepository.saveAndFlush(employee);

        return toResponse(updated);
    }

    // DYNAMIC SEARCH AND FILTERING
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> searchEmployees(
            String name,
            Long departmentId,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String designation,
            Pageable pageable) {

        if (minSalary != null && maxSalary != null
                && minSalary.compareTo(maxSalary) > 0) {
            throw new IllegalArgumentException(
                "Minimum salary cannot be greater than maximum salary"
            );
        }

        Specification<Employee> specification =
            EmployeeSpecification.filterEmployees(
                name,
                departmentId,
                minSalary,
                maxSalary,
                designation
            );

        return employeeRepository.findAll(specification, pageable)
            .map(this::toResponse);
    }

    // DELETE EMPLOYEE
    @Transactional
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Employee not found with ID: " + id
            ));

        if (assignmentRepository.existsByEmployee_Id(id)) {
            throw new ConflictException(
                "Cannot delete employee with active project assignments"
            );
        }

        employeeRepository.delete(employee);
    }

    // CONVERT ENTITY TO RESPONSE DTO
    private EmployeeResponse toResponse(Employee employee) {

        return new EmployeeResponse(
            employee.getId(),
            employee.getName(),
            employee.getEmail(),
            employee.getSalary(),
            employee.getDesignation(),
            employee.getDepartment().getId(),
            employee.getDepartment().getName(),
            employee.getVersion()
        );
    }
}
