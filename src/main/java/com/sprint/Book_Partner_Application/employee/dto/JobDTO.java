package com.sprint.Book_Partner_Application.employee.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class JobDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Job description is required")
        private String jobDesc;

        @Min(value = 10, message = "Min level must be at least 10")
        private int minLvl;

        @Max(value = 250, message = "Max level must not exceed 250")
        private int maxLvl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Short jobId;
        private String jobDesc;
        private int minLvl;
        private int maxLvl;
    }
}