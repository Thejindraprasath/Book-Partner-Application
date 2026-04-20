package com.sprint.Book_Partner_Application.employee.dto.response;



import java.time.LocalDateTime;



public class EmployeeResponse {
    private String empId;
    private String fname;
    private String minit;
    private String lname;

    private Short jobId;
    private String jobDesc;

    private Integer jobLvl;

    private String pubId;

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

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
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

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }

    private String pubName;

    private LocalDateTime hireDate;

    public EmployeeResponse() {
    }

    public EmployeeResponse(String empId, String fname, String minit, String lname, Short jobId, String jobDesc, Integer jobLvl, String pubId, String pubName, LocalDateTime hireDate) {
        this.empId = empId;
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
        this.jobId = jobId;
        this.jobDesc = jobDesc;
        this.jobLvl = jobLvl;
        this.pubId = pubId;
        this.pubName = pubName;
        this.hireDate = hireDate;
    }
}
