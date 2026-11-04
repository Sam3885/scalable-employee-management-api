
package com.company.employee.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee.dto.AssignmentRequest;
import com.company.employee.dto.AssignmentResponse;
import com.company.employee.entity.Employee;
import com.company.employee.entity.Project;
import com.company.employee.entity.EmployeeProject;
import com.company.employee.repository.EmployeeRepository;
import com.company.employee.repository.ProjectRepository;
import com.company.employee.repository.EmployeeProjectRepository;
import com.company.employee.exception.ConflictException;

@Service
public class AssignmentService {

    private final EmployeeProjectRepository assignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    public AssignmentService(
            EmployeeProjectRepository assignmentRepository,
            EmployeeRepository employeeRepository,
            ProjectRepository projectRepository) {

        this.assignmentRepository = assignmentRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    // CREATE ASSIGNMENT
    @Transactional
    public AssignmentResponse assignEmployee(AssignmentRequest request) {

        // Check employee
        Employee employee = employeeRepository
                .findById(request.employeeId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Employee not found"));

        // Check project
        Project project = projectRepository
                .findById(request.projectId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Project not found"));

        // Prevent duplicate assignment
        if (assignmentRepository.existsByEmployee_IdAndProject_Id(
                request.employeeId(), request.projectId())) {

            throw new ConflictException(
                    "Employee is already assigned to this project");
        }

        EmployeeProject assignment = new EmployeeProject();

        assignment.setEmployee(employee);
        assignment.setProject(project);
        assignment.setStatus("ACTIVE");

        EmployeeProject saved = assignmentRepository.saveAndFlush(assignment);

        return toResponse(saved);
    }

    // GET ASSIGNMENTS BY EMPLOYEE
    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignmentsByEmployee(Long employeeId) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new NoSuchElementException("Employee not found");
        }

        return assignmentRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // GET ASSIGNMENTS BY PROJECT
    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignmentsByProject(Long projectId) {

        if (!projectRepository.existsById(projectId)) {
            throw new NoSuchElementException("Project not found");
        }

        return assignmentRepository.findByProject_Id(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // CONVERT ENTITY TO DTO
    private AssignmentResponse toResponse(EmployeeProject assignment) {

        return new AssignmentResponse(
                assignment.getId(),
                assignment.getEmployee().getId(),
                assignment.getEmployee().getName(),
                assignment.getProject().getId(),
                assignment.getProject().getName(),
                assignment.getAssignedAt(),
                assignment.getStatus()
        );
    }
}
