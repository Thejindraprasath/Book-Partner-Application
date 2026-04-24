package com.sprint.Book_Partner_Application.author.dto.response;

public class TitleAuthorResponse {

    private String auId;
    private String authorName;
    private String titleId;
    private String titleName;
    private Short auOrd;
    private Integer royaltyper;

    // ✅ No-args constructor
    public TitleAuthorResponse() {}

    // ✅ all-args constructor
    public TitleAuthorResponse(String auId, String authorName,
                               String titleId, String titleName,
                               Short auOrd, Integer royaltyper) {
        this.auId = auId;
        this.authorName = authorName;
        this.titleId = titleId;
        this.titleName = titleName;
        this.auOrd = auOrd;
        this.royaltyper = royaltyper;
    }

    // Getters and Setters

    public String getAuId() { return auId; }
    public void setAuId(String auId) { this.auId = auId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getTitleId() { return titleId; }
    public void setTitleId(String titleId) { this.titleId = titleId; }

    public String getTitleName() { return titleName; }
    public void setTitleName(String titleName) { this.titleName = titleName; }

    public Short getAuOrd() { return auOrd; }
    public void setAuOrd(Short auOrd) { this.auOrd = auOrd; }

    public Integer getRoyaltyper() { return royaltyper; }
    public void setRoyaltyper(Integer royaltyper) {
        this.royaltyper = royaltyper;
    }
}