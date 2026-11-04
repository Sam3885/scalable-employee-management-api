package com.company.employee.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssignmentRequest(

    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be positive")
    Long employeeId,

    @NotNull(message = "Project ID is required")
    @Positive(message = "Project ID must be positive")
    Long projectId

) {
}