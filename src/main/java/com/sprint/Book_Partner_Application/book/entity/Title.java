package com.sprint.Book_Partner_Application.book.entity;

import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "titles")
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

    public Title() {
    }

    public Title(String titleId, String title, String type, Publisher publisher, Double price, Double advance, Integer royalty, Integer ytdSales, String notes, LocalDateTime pubdate, List<TitleAuthor> titleAuthors, List<RoySched> royScheds, List<Sale> sales) {
        this.titleId = titleId;
        this.title = title;
        this.type = type;
        this.publisher = publisher;
        this.price = price;
        this.advance = advance;
        this.royalty = royalty;
        this.ytdSales = ytdSales;
        this.notes = notes;
        this.pubdate = pubdate;
        this.titleAuthors = titleAuthors;
        this.royScheds = royScheds;
        this.sales = sales;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
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

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
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

    public List<TitleAuthor> getTitleAuthors() {
        return titleAuthors;
    }

    public void setTitleAuthors(List<TitleAuthor> titleAuthors) {
        this.titleAuthors = titleAuthors;
    }

    public List<RoySched> getRoyScheds() {
        return royScheds;
    }

    public void setRoyScheds(List<RoySched> royScheds) {
        this.royScheds = royScheds;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
}