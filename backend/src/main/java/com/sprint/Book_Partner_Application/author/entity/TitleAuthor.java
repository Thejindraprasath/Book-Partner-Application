package com.sprint.Book_Partner_Application.author.entity;

import com.sprint.Book_Partner_Application.book.entity.Title;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "titleauthor")
@IdClass(TitleAuthor.TitleAuthorId.class)
public class TitleAuthor {

    @Id
    @Column(name = "au_id", length = 11)
    private String auId;

    @Id
    @Column(name = "title_id", length = 10)
    private String titleId;

    @ManyToOne
    @JoinColumn(name = "au_id", insertable = false, updatable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "title_id", insertable = false, updatable = false)
    private Title title;

    @Column(name = "au_ord")
    private Short auOrd;

    @Column(name = "royaltyper")
    private Integer royaltyper;

    // ===== Constructors =====

    public TitleAuthor() {}

    public TitleAuthor(String auId, String titleId,
                       Short auOrd, Integer royaltyper) {
        this.auId = auId;
        this.titleId = titleId;
        this.auOrd = auOrd;
        this.royaltyper = royaltyper;
    }

    // ===== Getters & Setters =====

    public String getAuId() { return auId; }
    public void setAuId(String auId) { this.auId = auId; }

    public String getTitleId() { return titleId; }
    public void setTitleId(String titleId) { this.titleId = titleId; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public Title getTitle() { return title; }
    public void setTitle(Title title) { this.title = title; }

    public Short getAuOrd() { return auOrd; }
    public void setAuOrd(Short auOrd) { this.auOrd = auOrd; }

    public Integer getRoyaltyper() { return royaltyper; }
    public void setRoyaltyper(Integer royaltyper) {
        this.royaltyper = royaltyper;
    }

    // ===== Composite Key Class =====

    public static class TitleAuthorId implements Serializable {

        private String auId;
        private String titleId;

        public TitleAuthorId() {}

        public TitleAuthorId(String auId, String titleId) {
            this.auId = auId;
            this.titleId = titleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TitleAuthorId)) return false;
            TitleAuthorId that = (TitleAuthorId) o;
            return Objects.equals(auId, that.auId) &&
                    Objects.equals(titleId, that.titleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(auId, titleId);
        }
    }
}