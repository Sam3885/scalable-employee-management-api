package com.company.employee.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

public record EmployeeRequest(

    @NotBlank(message = "Employee name is required")
    @Size(max = 100)
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Size(max = 150)
    String email,

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.01", message = "Salary must be positive")
    BigDecimal salary,

    @NotBlank(message = "Designation is required")
    @Size(max = 100)
    String designation,

    @NotNull(message = "Department ID is required")
    @Positive(message = "Department ID must be positive")
    Long departmentId

) {}