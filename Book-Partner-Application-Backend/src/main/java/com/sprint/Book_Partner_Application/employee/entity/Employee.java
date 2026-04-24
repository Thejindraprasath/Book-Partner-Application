package com.sprint.Book_Partner_Application.employee.entity;

import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "employee")

public class Employee {

    @Id
    @Column(name = "emp_id", length = 10, nullable = false)
    @NotBlank(message = "Employee ID is required")
    @Pattern(
            regexp = "^[A-Z]{3}[1-9][0-9]{4}[FM]$|^[A-Z]-[A-Z][1-9][0-9]{4}[FM]$",
            message = "Invalid employee ID format"
    )
    private String empId;

    @Column(name = "fname", nullable = false, length = 20)
    @NotBlank(message = "First name is required")
    @Size(max = 20, message = "First name must not exceed 20 characters")
    private String fname;

    @Column(name = "minit", length = 1)
    @Size(max = 1, message = "Middle initial must be 1 character")
    private String minit;

    @Column(name = "lname", nullable = false, length = 30)
    @NotBlank(message = "Last name is required")
    @Size(max = 30, message = "Last name must not exceed 30 characters")
    private String lname;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    @NotNull(message = "Job is required")
    private Job job;

    @Column(name = "job_lvl")
    private Integer jobLvl = 10;

    @ManyToOne
    @JoinColumn(name = "pub_id", nullable = false)
    @NotNull(message = "Publisher is required")
    private Publisher publisher;

    @Column(name = "hire_date", nullable = false)
    private LocalDateTime hireDate = LocalDateTime.now();

    // ================= MANUAL CONSTRUCTORS =================

    // No-Args Constructor (JPA Required)
    public Employee() {
    }

    // Clean Constructor (Best Practice)
    public Employee(String empId, String fname, String minit, String lname,
                    Job job, Integer jobLvl,
                    Publisher publisher, LocalDateTime hireDate) {

        this.empId = empId;
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
        this.job = job;
        this.jobLvl = (jobLvl != null) ? jobLvl : 10;
        this.publisher = publisher;
        this.hireDate = (hireDate != null) ? hireDate : LocalDateTime.now();
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMinit() {
        return minit;
    }

    public void setMinit(String minit) {
        this.minit = minit;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Integer getJobLvl() {
        return jobLvl;
    }

    public void setJobLvl(Integer jobLvl) {
        this.jobLvl = jobLvl;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }
}