package com.sprint.Book_Partner_Application.book.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

public class TitleDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Title ID is required")
        private String titleId;

        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Type is required")
        private String type;

        private String pubId;
        private Double price;
        private Double advance;
        private Integer royalty;
        private Integer ytdSales;
        private String notes;

        @NotNull(message = "Publish date is required")
        private LocalDateTime pubdate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String titleId;
        private String title;
        private String type;
        private String pubId;
        private String pubName;
        private Double price;
        private Double advance;
        private Integer royalty;
        private Integer ytdSales;
        private String notes;
        private LocalDateTime pubdate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String title;
        private String type;
        private String pubId;
        private Double price;
        private Double advance;
        private Integer royalty;
        private Integer ytdSales;
        private String notes;
        private LocalDateTime pubdate;
    }
}
