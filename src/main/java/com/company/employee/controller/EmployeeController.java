
package com.company.employee.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.company.employee.dto.EmployeeRequest;
import com.company.employee.dto.EmployeeResponse;
import com.company.employee.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // CREATE EMPLOYEE
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response =
            employeeService.createEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    // GET EMPLOYEE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployee(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            employeeService.getEmployeeById(id)
        );
    }

    // GET ALL EMPLOYEES WITH PAGINATION
    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
            @PageableDefault(
                size = 10,
                sort = "id",
                direction = Sort.Direction.ASC
            ) Pageable pageable) {

        return ResponseEntity.ok(
            employeeService.getAllEmployees(pageable)
        );
    }

    // UPDATE EMPLOYEE
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
            employeeService.updateEmployee(id, request)
        );
    }

    // DELETE EMPLOYEE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    // SEARCH AND FILTER EMPLOYEES
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(

            @RequestParam(required = false) String name,

            @RequestParam(required = false) Long departmentId,

            @RequestParam(required = false) BigDecimal minSalary,

            @RequestParam(required = false) BigDecimal maxSalary,

            @RequestParam(required = false) String designation,

            @PageableDefault(
                size = 10,
                sort = "id",
                direction = Sort.Direction.ASC
            ) Pageable pageable) {

        return ResponseEntity.ok(
            employeeService.searchEmployees(
                name,
                departmentId,
                minSalary,
                maxSalary,
                designation,
                pageable
            )
        );
    }
}

