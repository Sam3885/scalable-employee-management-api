
package com.company.employee.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProjectRequest(

    @NotBlank(message = "Project name is required")
    @Size(max = 150)
    String name,

    @Size(max = 500)
    String description,

    @NotBlank(message = "Project status is required")
    @Pattern(
        regexp = "ACTIVE|COMPLETED|ON_HOLD",
        message = "Status must be ACTIVE, COMPLETED or ON_HOLD"
    )
    String status,

    @DecimalMin("0.0")
    BigDecimal budget,

    LocalDate startDate

) {
}

