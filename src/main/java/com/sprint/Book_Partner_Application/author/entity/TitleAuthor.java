package com.sprint.Book_Partner_Application.author.entity;

import com.sprint.Book_Partner_Application.book.entity.Title;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "titleauthor")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TitleAuthorId implements Serializable {
        private String auId;
        private String titleId;
    }
}