package com.sprint.Book_Partner_Application.author.dto.request;





import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class TitleAuthorCreateRequest {

    @NotBlank(message = "Author ID is required")
    private String auId;

    @NotBlank(message = "Title ID is required")
    private String titleId;

    private Short auOrd;
    private Integer royaltyper;

    public TitleAuthorCreateRequest() {}

    @Builder
    public TitleAuthorCreateRequest(String auId, String titleId,
                                    Short auOrd, Integer royaltyper) {
        this.auId = auId;
        this.titleId = titleId;
        this.auOrd = auOrd;
        this.royaltyper = royaltyper;
    }

    // Getters and Setters
    public String getAuId() { return auId; }
    public void setAuId(String auId) { this.auId = auId; }

    public String getTitleId() { return titleId; }
    public void setTitleId(String titleId) { this.titleId = titleId; }

    public Short getAuOrd() { return auOrd; }
    public void setAuOrd(Short auOrd) { this.auOrd = auOrd; }

    public Integer getRoyaltyper() { return royaltyper; }
    public void setRoyaltyper(Integer royaltyper) { this.royaltyper = royaltyper; }
}