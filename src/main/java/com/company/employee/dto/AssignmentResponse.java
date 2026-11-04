package com.company.employee.dto;

import java.time.LocalDateTime;

public record AssignmentResponse(
    Long id,
    Long employeeId,
    String employeeName,
    Long projectId,
    String projectName,
    LocalDateTime assignedAt,
    String status
) {
}