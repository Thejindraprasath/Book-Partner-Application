package com.sprint.Book_Partner_Application.book.dto.response;

import lombok.*;

@Builder
public class RoySchedResponse {
    private Long roySchedId;
    private String titleId;
    private String titleName;
    private Integer lorange;
    private Integer hirange;
    private Integer royalty;

    public RoySchedResponse() {
    }

    public RoySchedResponse(Long roySchedId, String titleId, String titleName, Integer lorange, Integer hirange, Integer royalty) {
        this.roySchedId = roySchedId;
        this.titleId = titleId;
        this.titleName = titleName;
        this.lorange = lorange;
        this.hirange = hirange;
        this.royalty = royalty;
    }

    public Long getRoySchedId() {
        return roySchedId;
    }

    public void setRoySchedId(Long roySchedId) {
        this.roySchedId = roySchedId;
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
