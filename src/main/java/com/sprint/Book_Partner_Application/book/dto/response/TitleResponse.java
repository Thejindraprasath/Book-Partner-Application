package com.sprint.Book_Partner_Application.book.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
public class TitleResponse {
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

    public TitleResponse() {
    }

    public TitleResponse(String titleId, String title, String type, String pubId, String pubName, Double price, Double advance, Integer royalty, Integer ytdSales, String notes, LocalDateTime pubdate) {
        this.titleId = titleId;
        this.title = title;
        this.type = type;
        this.pubId = pubId;
        this.pubName = pubName;
        this.price = price;
        this.advance = advance;
        this.royalty = royalty;
        this.ytdSales = ytdSales;
        this.notes = notes;
        this.pubdate = pubdate;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPubId() {
        return pubId;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public Integer getRoyalty() {
        return royalty;
    }

    public void setRoyalty(Integer royalty) {
        this.royalty = royalty;
    }

    public Integer getYtdSales() {
        return ytdSales;
    }

    public void setYtdSales(Integer ytdSales) {
        this.ytdSales = ytdSales;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getPubdate() {
        return pubdate;
    }

    public void setPubdate(LocalDateTime pubdate) {
        this.pubdate = pubdate;
    }
}
