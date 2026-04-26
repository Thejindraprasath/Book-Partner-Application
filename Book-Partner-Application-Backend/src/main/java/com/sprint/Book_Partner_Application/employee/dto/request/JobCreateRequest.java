package com.sprint.Book_Partner_Application.employee.dto.request;

import jakarta.validation.constraints.*;


public class JobCreateRequest {

    private String jobDesc;

    @Min(value = 10, message = "Minimum level must be at least 10")
    private Integer minLvl;

    @Max(value = 250, message = "Maximum level must not exceed 250")
    private Integer maxLvl;

    public JobCreateRequest() {
    }

    public JobCreateRequest(String jobDesc, Integer minLvl, Integer maxLvl) {
        this.jobDesc = jobDesc;
        this.minLvl = minLvl;
        this.maxLvl = maxLvl;
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
}
