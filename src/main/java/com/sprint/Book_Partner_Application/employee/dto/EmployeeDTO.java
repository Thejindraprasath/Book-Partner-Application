package com.sprint.Book_Partner_Application.employee.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

public class EmployeeDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Employee ID is required")
        @Pattern(regexp = "^[A-Z]{3}[1-9][0-9]{4}[FM]$|^[A-Z]-[A-Z][1-9][0-9]{4}[FM]$",
                message = "Invalid employee ID format")
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
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String empId;
        private String fname;
        private String minit;
        private String lname;
        private Short jobId;
        private String jobDesc;
        private Integer jobLvl;
        private String pubId;
        private String pubName;
        private LocalDateTime hireDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String fname;
        private String minit;
        private String lname;
        private Short jobId;
        private Integer jobLvl;
        private String pubId;
        private LocalDateTime hireDate;
    }
}