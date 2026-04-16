package com.sprint.Book_Partner_Application.sales.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

public class SaleDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Store ID is required")
        private String storId;

        @NotBlank(message = "Order number is required")
        private String ordNum;

        @NotNull(message = "Order date is required")
        private LocalDateTime ordDate;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Short qty;

        @NotBlank(message = "Payment terms are required")
        private String payterms;

        @NotBlank(message = "Title ID is required")
        private String titleId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String storId;
        private String storName;
        private String ordNum;
        private LocalDateTime ordDate;
        private Short qty;
        private String payterms;
        private String titleId;
        private String titleName;
    }
}