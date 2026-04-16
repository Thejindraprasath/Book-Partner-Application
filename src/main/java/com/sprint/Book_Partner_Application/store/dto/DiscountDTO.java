package com.sprint.Book_Partner_Application.store.dto;


import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

public class DiscountDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Discount type is required")
        private String discounttype;

        private String storId;
        private Short lowqty;
        private Short highqty;

        @NotNull(message = "Discount value is required")
        @DecimalMin(value = "0.0", message = "Discount must be positive")
        private BigDecimal discount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long discountId;
        private String discounttype;
        private String storId;
        private String storName;
        private Short lowqty;
        private Short highqty;
        private BigDecimal discount;
    }
}