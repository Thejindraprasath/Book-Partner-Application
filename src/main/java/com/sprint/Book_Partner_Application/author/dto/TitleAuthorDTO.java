package com.sprint.Book_Partner_Application.author.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class TitleAuthorDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Author ID is required")
        private String auId;

        @NotBlank(message = "Title ID is required")
        private String titleId;

        private Short auOrd;
        private Integer royaltyper;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String auId;
        private String authorName;
        private String titleId;
        private String titleName;
        private Short auOrd;
        private Integer royaltyper;
    }
}
