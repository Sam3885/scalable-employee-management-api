package com.company.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.employee.entity.Project;

public interface ProjectRepository
        extends JpaRepository<Project, Long> {

    List<Project> findByStatus(String status);

    List<Project> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}
