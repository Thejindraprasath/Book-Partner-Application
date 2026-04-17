package com.sprint.Book_Partner_Application.book.dto.request;

import lombok.*;

@Builder
public class RoySchedUpdateRequest {
    private String titleId;
    private Integer lorange;
    private Integer hirange;
    private Integer royalty;

    public RoySchedUpdateRequest() {
    }

    public RoySchedUpdateRequest(String titleId, Integer lorange, Integer hirange, Integer royalty) {
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
