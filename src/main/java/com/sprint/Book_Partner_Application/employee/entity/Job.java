package com.sprint.Book_Partner_Application.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Short jobId;

    @Column(name = "job_desc", nullable = false, length = 50)
    @NotBlank(message = "Job description is required")
    @Size(max = 50, message = "Job description must not exceed 50 characters")
    @Builder.Default
    private String jobDesc = "New Position - title not formalized yet";

    @Column(name = "min_lvl", nullable = false)
    @NotNull(message = "Minimum level is required")
    @Min(value = 10, message = "Minimum level must be at least 10")
    private Integer minLvl;

    @Column(name = "max_lvl", nullable = false)
    @NotNull(message = "Maximum level is required")
    @Max(value = 250, message = "Maximum level must not exceed 250")
    private Integer maxLvl;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Employee> employees = new ArrayList<>();

    public Job() {
    }

    public Job(Short jobId, String jobDesc, Integer minLvl, Integer maxLvl, List<Employee> employees) {
        this.jobId = jobId;
        this.jobDesc = jobDesc;
        this.minLvl = minLvl;
        this.maxLvl = maxLvl;
        this.employees = employees;
    }

    public Short getJobId() {
        return jobId;
    }

    public void setJobId(Short jobId) {
        this.jobId = jobId;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public Integer getMinLvl() {
        return minLvl;
    }

    public void setMinLvl(Integer minLvl) {
        this.minLvl = minLvl;
    }

    public Integer getMaxLvl() {
        return maxLvl;
    }

    public void setMaxLvl(Integer maxLvl) {
        this.maxLvl = maxLvl;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
