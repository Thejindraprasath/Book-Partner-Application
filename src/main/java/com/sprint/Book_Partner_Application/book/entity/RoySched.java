package com.sprint.Book_Partner_Application.book.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roysched")
@Builder
public class RoySched {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roysched_id")
    private Long roySchedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Title title;

    @Column(name = "lorange")
    private Integer lorange;

    @Column(name = "hirange")
    private Integer hirange;

    @Column(name = "royalty")
    private Integer royalty;

    // ================= MANUAL CONSTRUCTORS =================

    // No-Args Constructor (Required by JPA)
    public RoySched() {
    }

    // Constructor WITHOUT relationship (Recommended)
    public RoySched(Long roySchedId, Integer lorange, Integer hirange, Integer royalty) {
        this.roySchedId = roySchedId;
        this.lorange = lorange;
        this.hirange = hirange;
        this.royalty = royalty;
    }

    // Optional: Constructor WITH relationship (use carefully)
    public RoySched(Long roySchedId, Title title,
                    Integer lorange, Integer hirange, Integer royalty) {
        this.roySchedId = roySchedId;
        this.title = title;
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

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
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