package com.sprint.Book_Partner_Application.author.entity;

import com.sprint.Book_Partner_Application.book.entity.Title;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "titleauthor")
@Data
@Builder
@IdClass(TitleAuthor.TitleAuthorId.class)
public class TitleAuthor {

    @Id
    @Column(name = "au_id", length = 11)
    private String auId;

    @Id
    @Column(name = "title_id", length = 10)
    private String titleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "au_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Title title;

    @Column(name = "au_ord")
    private Short auOrd;

    @Column(name = "royaltyper")
    private Integer royaltyper;

    // ================= MANUAL CONSTRUCTORS =================

    // No-Args Constructor (Required by JPA)
    public TitleAuthor() {
    }

    // Constructor WITHOUT relationships (Recommended)
    public TitleAuthor(String auId, String titleId,
                       Short auOrd, Integer royaltyper) {
        this.auId = auId;
        this.titleId = titleId;
        this.auOrd = auOrd;
        this.royaltyper = royaltyper;
    }

    // Optional: Full constructor (NOT recommended for regular use)
    public TitleAuthor(String auId, String titleId,
                       Author author, Title title,
                       Short auOrd, Integer royaltyper) {
        this.auId = auId;
        this.titleId = titleId;
        this.author = author;
        this.title = title;
        this.auOrd = auOrd;
        this.royaltyper = royaltyper;
    }

    // ================= COMPOSITE KEY CLASS =================

    @Data
    public static class TitleAuthorId implements Serializable {

        private String auId;
        private String titleId;

        // No-Args Constructor
        public TitleAuthorId() {
        }

        // All-Args Constructor
        public TitleAuthorId(String auId, String titleId) {
            this.auId = auId;
            this.titleId = titleId;
        }

        // Explicit equals & hashCode (Best Practice for IdClass)

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