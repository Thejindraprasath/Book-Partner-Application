package com.sprint.Book_Partner_Application.employee.dto.response;


public class JobResponse {
    private Short jobId;
    private String jobDesc;
    private int minLvl;
    private int maxLvl;

    public JobResponse() {
    }

    public JobResponse(Short jobId, String jobDesc, int minLvl, int maxLvl) {
        this.jobId = jobId;
        this.jobDesc = jobDesc;
        this.minLvl = minLvl;
        this.maxLvl = maxLvl;
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
