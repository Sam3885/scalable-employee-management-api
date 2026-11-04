
package com.company.employee.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "projects", uniqueConstraints = {
    @UniqueConstraint(name = "uk_project_name", columnNames = "name")
})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 30)
    private String status = "ACTIVE";

    @Column(precision = 14, scale = 2)
    private BigDecimal budget;

    private LocalDate startDate;

    
    @OneToMany(mappedBy = "project")
    private List<EmployeeProject> assignments = new ArrayList<>();

     @Version
    private Long version;

    public Project() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getBudget() {
		return budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public List<EmployeeProject> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<EmployeeProject> assignments) {
		this.assignments = assignments;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	

    
}
