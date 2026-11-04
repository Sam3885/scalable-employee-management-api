package com.company.employee.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee.dto.DepartmentRequest;
import com.company.employee.dto.DepartmentResponse;
import com.company.employee.entity.Department;
import com.company.employee.repository.DepartmentRepository;
import com.company.employee.exception.ConflictException;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {

        if (departmentRepository.existsByName(request.name())) {
            throw new ConflictException(
                "Department already exists"
            );
        }

        Department department = new Department();

        department.setName(request.name());
        department.setDescription(request.description());

        Department saved = departmentRepository.save(department);

        return new DepartmentResponse(
            saved.getId(),
            saved.getName(),
            saved.getDescription()
        );
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {

        return departmentRepository.findAll()
            .stream()
            .map(department -> new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription()
            ))
            .toList();
    }
}