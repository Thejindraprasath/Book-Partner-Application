package com.sprint.Book_Partner_Application.store.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class DiscountCreateRequest {

    @NotBlank(message = "Discount type is required")
    private String discounttype;

    @NotBlank(message = "Store ID is required")
    private String storId;

    @NotNull(message = "Low quantity is required")
    @Min(value = 0, message = "Low quantity must be >= 0")
    private Integer lowqty;

    @NotNull(message = "High quantity is required")
    @Min(value = 1, message = "High quantity must be >= 1")
    private Integer highqty;

    @NotNull(message = "Discount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount must be > 0")
    private BigDecimal discount;

    public String getDiscounttype() {
        return discounttype;
    }

    public void setDiscounttype(String discounttype) {
        this.discounttype = discounttype;
    }

    public String getStorId() {
        return storId;
    }

    public void setStorId(String storId) {
        this.storId = storId;
    }

    public Integer getLowqty() {
        return lowqty;
    }

    public void setLowqty(Integer lowqty) {
        this.lowqty = lowqty;
    }

    public Integer getHighqty() {
        return highqty;
    }

    public void setHighqty(Integer highqty) {
        this.highqty = highqty;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public DiscountCreateRequest() {
    }

    public DiscountCreateRequest(String discounttype, String storId, Integer lowqty, Integer highqty, BigDecimal discount) {
        this.discounttype = discounttype;
        this.storId = storId;
        this.lowqty = lowqty;
        this.highqty = highqty;
        this.discount = discount;
    }
}