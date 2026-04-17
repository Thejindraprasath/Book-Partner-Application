package com.sprint.Book_Partner_Application.employee.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeUpdateRequest {
    private String fname;
    private String minit;
    private String lname;
    private Short  jobId;
    private Integer jobLvl;
    private String pubId;
    private LocalDateTime hireDate;

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

    public EmployeeUpdateRequest() {
    }

    public EmployeeUpdateRequest(String fname, String minit, String lname, Short jobId, Integer jobLvl, String pubId, LocalDateTime hireDate) {
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
        this.jobId = jobId;
        this.jobLvl = jobLvl;
        this.pubId = pubId;
        this.hireDate = hireDate;
    }
}