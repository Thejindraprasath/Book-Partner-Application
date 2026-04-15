package com.sprint.Book_Partner_Application.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "titles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Title {
    @Id
    @Column(name = "title_id", length = 10)
    @NotBlank(message = "Title ID is required")
    private String titleId;

    @Column(name = "title", nullable = false, length = 80)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(name = "type", nullable = false, length = 12)
    @NotBlank(message = "Type is required")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude

    private publishers publisher;

    @Column(name = "price")
    private Double price;

    @Column(name = "advance")
    private Double advance;

    @Column(name = "royalty")
    private Integer royalty;

    @Column(name = "ytd_sales")
    private Integer ytdSales;

    @Column(name = "notes", length = 200)
    private String notes;

    @Column(name = "pubdate", nullable = false)
    @NotNull(message = "Publish date is required")
    private LocalDateTime pubdate;

    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<TitleAuthor> titleAuthors = new ArrayList<>();

    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<RoySched> royScheds = new ArrayList<>();

    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Sale> sales = new ArrayList<>();@Id
    @Column(name = "title_id", length = 10)
    @NotBlank(message = "Title ID is required")
    private String titleId;

    @Column(name = "title", nullable = false, length = 80)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(name = "type", nullable = false, length = 12)
    @NotBlank(message = "Type is required")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude

    private Publisher publisher;

    @Column(name = "price")
    private Double price;

    @Column(name = "advance")
    private Double advance;

    @Column(name = "royalty")
    private Integer royalty;

    @Column(name = "ytd_sales")
    private Integer ytdSales;

    @Column(name = "notes", length = 200)
    private String notes;

    @Column(name = "pubdate", nullable = false)
    @NotNull(message = "Publish date is required")
    private LocalDateTime pubdate;

    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<TitleAuthor> titleAuthors = new ArrayList<>();

    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<RoySched> royScheds = new ArrayList<>();

    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Sale> sales = new ArrayList<>();
}
