package com.sprint.Book_Partner_Application.store.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class StoreDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Store ID is required")
        private String storId;

        private String storName;
        private String storAddress;
        private String city;
        private String state;

        @Pattern(regexp = "^[0-9]{5}$", message = "Zip must be 5 digits")
        private String zip;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String storId;
        private String storName;
        private String storAddress;
        private String city;
        private String state;
        private String zip;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String storName;
        private String storAddress;
        private String city;
        private String state;

        @Pattern(regexp = "^[0-9]{5}$", message = "Zip must be 5 digits")
        private String zip;
    }
}