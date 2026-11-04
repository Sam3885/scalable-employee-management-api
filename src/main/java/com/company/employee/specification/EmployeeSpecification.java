
package com.company.employee.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.company.employee.entity.Employee;

import jakarta.persistence.criteria.Predicate;

public class EmployeeSpecification {

    public static Specification<Employee> filterEmployees(
            String name,
            Long departmentId,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String designation) {

        return (root, query, criteriaBuilder) -> {

            Predicate predicate = criteriaBuilder.conjunction();

            // Filter by employee name
            if (name != null && !name.isBlank()) {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                    )
                );
            }

            // Filter by department
            if (departmentId != null) {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(
                        root.get("department").get("id"),
                        departmentId
                    )
                );
            }

            // Filter by minimum salary
            if (minSalary != null) {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("salary"),
                        minSalary
                    )
                );
            }

            // Filter by maximum salary
            if (maxSalary != null) {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("salary"),
                        maxSalary
                    )
                );
            }

            // Filter by designation
            if (designation != null && !designation.isBlank()) {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("designation")),
                        "%" + designation.toLowerCase() + "%"
                    )
                );
            }

            return predicate;
        };
    }
}
