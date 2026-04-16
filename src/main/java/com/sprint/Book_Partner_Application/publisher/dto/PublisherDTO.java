package com.sprint.Book_Partner_Application.publisher.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class PublisherDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Publisher ID is required")
        private String pubId;

        private String pubName;
        private String city;
        private String state;
        private String country;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String pubId;
        private String pubName;
        private String city;
        private String state;
        private String country;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String pubName;
        private String city;
        private String state;
        private String country;
    }
}