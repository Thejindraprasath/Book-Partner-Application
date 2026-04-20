package com.sprint.Book_Partner_Application.employee.dto.request;
import jakarta.validation.constraints.*;


import java.time.LocalDateTime;

public class EmployeeCreateRequest {
    @NotBlank(message = "Employee ID is required")
    @Pattern(
            regexp = "^[A-Z]{3}[1-9][0-9]{4}[FM]$|^[A-Z]-[A-Z][1-9][0-9]{4}[FM]$",
            message = "Employee ID must match AAA#####F/M or A-A#####F/M (e.g. PTC11962M)"
    )
    private String empId;

    @NotBlank(message = "First name is required")
    private String fname;

    private String minit;

    @NotBlank(message = "Last name is required")
    private String lname;

    @NotNull(message = "Job ID is required")
    private Short jobId;

    private Integer jobLvl;

    @NotBlank(message = "Publisher ID is required")
    private String pubId;

    private LocalDateTime hireDate;

    public EmployeeCreateRequest(String empId, String fname, String minit, String lname, Short jobId, Integer jobLvl, String pubId, LocalDateTime hireDate) {
        this.empId = empId;
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
        this.jobId = jobId;
        this.jobLvl = jobLvl;
        this.pubId = pubId;
        this.hireDate = hireDate;
    }



    public EmployeeCreateRequest() {
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

    public Short getJobId() {
        return jobId;
    }

    public void setJobId(Short jobId) {
        this.jobId = jobId;
    }

    public Integer getJobLvl() {
        return jobLvl;
    }

    public void setJobLvl(Integer jobLvl) {
        this.jobLvl = jobLvl;
    }

    public String getPubId() {
        return pubId;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }
}
