package com.sprint.Book_Partner_Application.book.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

public class TitleUpdateRequest {
    private String title;

    private String type;

    private String pubId;

    @Positive(message = "Price must be greater than 0")
    private Double price;

    @Positive(message = "Advance must be greater than 0")
    private Double advance;

    @Min(value = 0, message = "Royalty cannot be less than 0")
    @Max(value = 100, message = "Royalty cannot exceed 100")
    private Integer royalty;

    @Min(value = 0, message = "YTD Sales cannot be negative")
    private Integer ytdSales;

    @Size(max = 200, message = "Notes must not exceed 200 characters")
    private String notes;

    private LocalDateTime pubdate;

    public TitleUpdateRequest() {
    }

    public TitleUpdateRequest(String title, String type, String pubId, Double price, Double advance, Integer royalty, Integer ytdSales, String notes, LocalDateTime pubdate) {
        this.title = title;
        this.type = type;
        this.pubId = pubId;
        this.price = price;
        this.advance = advance;
        this.royalty = royalty;
        this.ytdSales = ytdSales;
        this.notes = notes;
        this.pubdate = pubdate;
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
