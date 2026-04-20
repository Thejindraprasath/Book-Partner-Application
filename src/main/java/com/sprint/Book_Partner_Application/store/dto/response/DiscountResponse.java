package com.sprint.Book_Partner_Application.store.dto.response;

import java.math.BigDecimal;

public class DiscountResponse {

    private Long discountId;
    private String discounttype;
    private String storId;
    private String storName;
    private Integer lowqty;
    private Integer highqty;
    private BigDecimal discount;

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

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

    public String getStorName() {
        return storName;
    }

    public void setStorName(String storName) {
        this.storName = storName;
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

    public DiscountResponse() {
    }

    public DiscountResponse(Long discountId, String discounttype, String storId, String storName, Integer lowqty, Integer highqty, BigDecimal discount) {
        this.discountId = discountId;
        this.discounttype = discounttype;
        this.storId = storId;
        this.storName = storName;
        this.lowqty = lowqty;
        this.highqty = highqty;
        this.discount = discount;
    }
}