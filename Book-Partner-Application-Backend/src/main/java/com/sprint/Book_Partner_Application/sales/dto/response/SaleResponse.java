package com.sprint.Book_Partner_Application.sales.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class SaleResponse {

    private String storId;
    private String storName;
    private String ordNum;
    private LocalDateTime ordDate;
    private Short qty;
    private String payterms;
    private String titleId;
    private String titleName;

    // 🔹 No-Args Constructor
    public SaleResponse() {
    }

    // 🔹 All-Args Constructor
    public SaleResponse(String storId, String storName, String ordNum,
                        LocalDateTime ordDate, Short qty, String payterms,
                        String titleId, String titleName) {
        this.storId = storId;
        this.storName = storName;
        this.ordNum = ordNum;
        this.ordDate = ordDate;
        this.qty = qty;
        this.payterms = payterms;
        this.titleId = titleId;
        this.titleName = titleName;
    }

    // 🔹 Getters and Setters

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

    public String getOrdNum() {
        return ordNum;
    }

    public void setOrdNum(String ordNum) {
        this.ordNum = ordNum;
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

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}
