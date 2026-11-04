
package com.company.employee.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.company.employee.dto.AssignmentRequest;
import com.company.employee.dto.AssignmentResponse;
import com.company.employee.service.AssignmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // ASSIGN EMPLOYEE TO PROJECT
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentResponse assignEmployee(
            @Valid @RequestBody AssignmentRequest request) {

        return assignmentService.assignEmployee(request);
    }

    // GET ASSIGNMENTS BY EMPLOYEE
    @GetMapping("/employee/{employeeId}")
    public List<AssignmentResponse> getAssignmentsByEmployee(
            @PathVariable Long employeeId) {

        return assignmentService.getAssignmentsByEmployee(employeeId);
    }

    // GET ASSIGNMENTS BY PROJECT
    @GetMapping("/project/{projectId}")
    public List<AssignmentResponse> getAssignmentsByProject(
            @PathVariable Long projectId) {

        return assignmentService.getAssignmentsByProject(projectId);
    }
}
