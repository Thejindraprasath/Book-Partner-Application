package com.sprint.Book_Partner_Application.book.entity;

import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "titles")
public class Title {

    // ================= PRIMARY KEY =================

    @Id
    @Column(name = "title_id", length = 10, nullable = false, unique = true)
    @NotBlank(message = "Title ID is required")
    @Size(max = 10, message = "Title ID must not exceed 10 characters")
    private String titleId;

    // ================= BASIC DETAILS =================

    @Column(name = "title", nullable = false, length = 80)
    @NotBlank(message = "Title is required")
    @Size(max = 80, message = "Title must not exceed 80 characters")
    private String title;

    @Column(name = "type", nullable = false, length = 12)
    @NotBlank(message = "Type is required")
    @Size(max = 12, message = "Type must not exceed 12 characters")
    private String type;

    // ================= RELATIONSHIPS =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    private Publisher publisher;

    // ================= NUMERIC FIELDS =================

    @Column(name = "price")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @Column(name = "advance")
    @PositiveOrZero(message = "Advance cannot be negative")
    private Double advance;

    @Column(name = "royalty")
    @Min(value = 0, message = "Royalty cannot be less than 0")
    @Max(value = 100, message = "Royalty cannot exceed 100")
    private Integer royalty;

    @Column(name = "ytd_sales")
    @Min(value = 0, message = "YTD sales cannot be negative")
    private Integer ytdSales;

    // ================= EXTRA DETAILS =================

    @Column(name = "notes", length = 200)
    @Size(max = 200, message = "Notes must not exceed 200 characters")
    private String notes;

    @Column(name = "pubdate", nullable = false)
    @NotNull(message = "Publish date is required")
    private LocalDateTime pubdate;

    // ================= CHILD RELATIONSHIPS =================

    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TitleAuthor> titleAuthors = new ArrayList<>();

    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoySched> royScheds = new ArrayList<>();

    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sale> sales = new ArrayList<>();

    // ================= CONSTRUCTORS =================

    public Title() {
    }

    public Title(String titleId, String title, String type, Publisher publisher,
                 Double price, Double advance, Integer royalty, Integer ytdSales,
                 String notes, LocalDateTime pubdate,
                 List<TitleAuthor> titleAuthors,
                 List<RoySched> royScheds,
                 List<Sale> sales) {
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

    // ================= GETTERS & SETTERS =================

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

    // ================= toString =================

    @Override
    public String toString() {
        return "Title{" +
                "titleId='" + titleId + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", advance=" + advance +
                ", royalty=" + royalty +
                ", ytdSales=" + ytdSales +
                ", notes='" + notes + '\'' +
                ", pubdate=" + pubdate +
                '}';
    }

    // ================= equals =================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Title)) return false;
        Title other = (Title) o;
        return titleId != null && titleId.equals(other.titleId);
    }

    // ================= hashCode =================

    @Override
    public int hashCode() {
        return titleId != null ? titleId.hashCode() : 0;
    }
}