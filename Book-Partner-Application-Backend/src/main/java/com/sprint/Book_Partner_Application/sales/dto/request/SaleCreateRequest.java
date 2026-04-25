package com.sprint.Book_Partner_Application.sales.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class SaleCreateRequest {

    @NotBlank(message = "Store ID is required")
    @Size(max = 4, message = "Store ID must not exceed 4 characters")
    private String storId;

    @NotBlank(message = "Order number is required")
    @Size(max = 20, message = "Order number must not exceed 20 characters")
    private String ordNum;

    @NotBlank(message = "Title ID is required")
    @Size(max = 10, message = "Title ID must not exceed 10 characters")
    private String titleId;

    @NotNull(message = "Order date is required")
    @PastOrPresent(message = "Order date cannot be in the future")
    private LocalDateTime ordDate;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Short qty;

    @NotBlank(message = "Payment terms are required")
    @Size(max = 12, message = "Payment terms must not exceed 12 characters")
    private String payterms;

    // 🔹 No-Args Constructor
    public SaleCreateRequest() {
    }

    // 🔹 All-Args Constructor
    public SaleCreateRequest(String storId, String ordNum, String titleId,
                             LocalDateTime ordDate, Short qty, String payterms) {
        this.storId = storId;
        this.ordNum = ordNum;
        this.titleId = titleId;
        this.ordDate = ordDate;
        this.qty = qty;
        this.payterms = payterms;
    }

    // 🔹 Getters and Setters

    public String getStorId() {
        return storId;
    }

    public void setStorId(String storId) {
        this.storId = storId;
    }

    public String getOrdNum() {
        return ordNum;
    }

    public void setOrdNum(String ordNum) {
        this.ordNum = ordNum;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public LocalDateTime getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(LocalDateTime ordDate) {
        this.ordDate = ordDate;
    }

    public Short getQty() {
        return qty;
    }

    public void setQty(Short qty) {
        this.qty = qty;
    }

    public String getPayterms() {
        return payterms;
    }

    public void setPayterms(String payterms) {
        this.payterms = payterms;
    }
}
