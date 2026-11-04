
package com.company.employee.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee.dto.ProjectRequest;
import com.company.employee.dto.ProjectResponse;
import com.company.employee.entity.Project;
import com.company.employee.repository.ProjectRepository;
import com.company.employee.repository.EmployeeProjectRepository;
import com.company.employee.exception.ConflictException;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeProjectRepository assignmentRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            EmployeeProjectRepository assignmentRepository) {

        this.projectRepository = projectRepository;
        this.assignmentRepository = assignmentRepository;
    }

    // CREATE PROJECT
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {

        if (projectRepository.existsByName(request.name())) {
            throw new ConflictException(
                "Project name already exists"
            );
        }

        Project project = new Project();

        project.setName(request.name());
        project.setDescription(request.description());
        project.setStatus(request.status());
        project.setBudget(request.budget());
        project.setStartDate(request.startDate());

        Project saved = projectRepository.save(project);

        return toResponse(saved);
    }

    // GET PROJECT BY ID
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Project not found with ID: " + id
            ));

        return toResponse(project);
    }

    // GET ALL PROJECTS
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {

        return projectRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    // SEARCH PROJECTS BY STATUS
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByStatus(String status) {

        return projectRepository.findByStatus(status)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    // UPDATE PROJECT
    @Transactional
    public ProjectResponse updateProject(
            Long id, ProjectRequest request) {

        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Project not found with ID: " + id
            ));

        if (!project.getName().equalsIgnoreCase(request.name())
                && projectRepository.existsByName(request.name())) {

            throw new ConflictException(
                "Project name already exists"
            );
        }

        project.setName(request.name());
        project.setDescription(request.description());
        project.setStatus(request.status());
        project.setBudget(request.budget());
        project.setStartDate(request.startDate());

        Project updated = projectRepository.saveAndFlush(project);

        return toResponse(updated);
    }

    // DELETE PROJECT
    @Transactional
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Project not found with ID: " + id
            ));

        if (assignmentRepository.existsByProject_Id(id)) {
            throw new ConflictException(
                "Cannot delete project with employee assignments"
            );
        }

        projectRepository.delete(project);
    }

    // ENTITY TO DTO CONVERSION
    private ProjectResponse toResponse(Project project) {

        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStatus(),
            project.getBudget(),
            project.getStartDate(),
            project.getVersion()
        );
    }
}
