package com.sprint.Book_Partner_Application.book.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class RoySchedCreateRequest {

    @NotBlank(message = "Title ID is required")
    private String titleId;

    @NotNull(message = "Lower range is required")
    @Min(value = 0, message = "Lower range cannot be negative")
    private Integer lorange;

    @NotNull(message = "Higher range is required")
    @Min(value = 0, message = "Higher range cannot be negative")
    private Integer hirange;

    @Min(value = 0, message = "Royalty cannot be less than 0")
    @Max(value = 100, message = "Royalty cannot exceed 100")
    private Integer royalty;

    public RoySchedCreateRequest() {
    }

    public RoySchedCreateRequest(String titleId, Integer lorange, Integer hirange, Integer royalty) {
        this.titleId = titleId;
        this.lorange = lorange;
        this.hirange = hirange;
        this.royalty = royalty;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public Integer getLorange() {
        return lorange;
    }

    public void setLorange(Integer lorange) {
        this.lorange = lorange;
    }

    public Integer getHirange() {
        return hirange;
    }

    public void setHirange(Integer hirange) {
        this.hirange = hirange;
    }

    public Integer getRoyalty() {
        return royalty;
    }

    public void setRoyalty(Integer royalty) {
        this.royalty = royalty;
    }
}
