
package com.company.employee.dto;

import java.math.BigDecimal;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
        BigDecimal salary,
        String designation,
        Long departmentId,
        String departmentName,
        Long version
) {
}
