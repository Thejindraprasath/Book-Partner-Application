package com.sprint.Book_Partner_Application.author.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class AuthorDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Author ID is required")
        @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$", message = "Author ID must match ###-##-####")
        private String auId;

        @NotBlank(message = "Last name is required")
        private String auLname;

        @NotBlank(message = "First name is required")
        private String auFname;

        @NotBlank(message = "Phone is required")
        private String phone;

        private String address;
        private String city;
        private String state;

        @Pattern(regexp = "^[0-9]{5}$", message = "Zip must be 5 digits")
        private String zip;

        private int contract;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String auId;
        private String auLname;
        private String auFname;
        private String phone;
        private String address;
        private String city;
        private String state;
        private String zip;
        private int contract;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String auLname;
        private String auFname;
        private String phone;
        private String address;
        private String city;
        private String state;

        @Pattern(regexp = "^[0-9]{5}$", message = "Zip must be 5 digits")
        private String zip;

        private Integer contract;
    }
}