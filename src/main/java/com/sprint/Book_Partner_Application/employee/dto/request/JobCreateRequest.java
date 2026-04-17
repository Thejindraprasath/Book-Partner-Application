package com.sprint.Book_Partner_Application.employee.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
@Builder
public class JobCreateRequest {

    @NotBlank(message = "Job description is required")
    private String jobDesc;

    @Min(value = 10, message = "Minimum level must be at least 10")
    private int minLvl;

    @Max(value = 250, message = "Maximum level must not exceed 250")
    private int maxLvl;

    public JobCreateRequest() {
    }

    public JobCreateRequest(String jobDesc, int minLvl, int maxLvl) {
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

    public int getMinLvl() {
        return minLvl;
    }

    public void setMinLvl(int minLvl) {
        this.minLvl = minLvl;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public void setMaxLvl(int maxLvl) {
        this.maxLvl = maxLvl;
    }
}
