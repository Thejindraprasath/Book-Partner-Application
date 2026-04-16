package com.sprint.Book_Partner_Application.book.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class RoySchedDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Title ID is required")
        private String titleId;

        private Integer lorange;
        private Integer hirange;
        private Integer royalty;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long roySchedId;
        private String titleId;
        private String titleName;
        private Integer lorange;
        private Integer hirange;
        private Integer royalty;
    }
}
