
package com.company.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.employee.entity.EmployeeProject;

public interface EmployeeProjectRepository
        extends JpaRepository<EmployeeProject, Long> {

    boolean existsByEmployee_IdAndProject_Id(
            Long employeeId, Long projectId);

    List<EmployeeProject> findByEmployee_Id(Long employeeId);

    List<EmployeeProject> findByProject_Id(Long projectId);

    boolean existsByEmployee_Id(Long employeeId);
    boolean existsByProject_Id(Long projectId);
}
