
package com.company.employee.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectResponse(
    Long id,
    String name,
    String description,
    String status,
    BigDecimal budget,
    LocalDate startDate,
    Long version
) {
}
