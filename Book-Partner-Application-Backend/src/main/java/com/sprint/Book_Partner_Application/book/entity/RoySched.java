package com.sprint.Book_Partner_Application.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "roysched")
public class RoySched {

    // ================= PRIMARY KEY =================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roysched_id")
    private Long roySchedId;

    // ================= RELATIONSHIP =================

    /**
     * Many royalty schedule entries can belong to one title.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "title_id", nullable = false)
    @NotNull(message = "Title is required")
    private Title title;

    @Column(name = "lorange", nullable = false)
    @NotNull(message = "Lower range is required")
    @Min(value = 0, message = "Lower range cannot be negative")
    private Integer lorange;

    @Column(name = "hirange", nullable = false)
    @NotNull(message = "Higher range is required")
    @Min(value = 0, message = "Higher range cannot be negative")
    private Integer hirange;

    @Column(name = "royalty")
    @Min(value = 0, message = "Royalty cannot be less than 0")
    @Max(value = 100, message = "Royalty cannot exceed 100")
    private Integer royalty;

    // ================= CONSTRUCTORS =================

    public RoySched() {
    }

    public RoySched(Long roySchedId, Integer lorange, Integer hirange, Integer royalty) {
        this.roySchedId = roySchedId;
        this.lorange = lorange;
        this.hirange = hirange;
        this.royalty = royalty;
    }

    public RoySched(Long roySchedId, Title title,
                    Integer lorange, Integer hirange, Integer royalty) {
        this.roySchedId = roySchedId;
        this.title = title;
        this.lorange = lorange;
        this.hirange = hirange;
        this.royalty = royalty;
    }

    // ================= GETTERS & SETTERS =================

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

    // ================= toString =================

    @Override
    public String toString() {
        return "RoySched{" +
                "roySchedId=" + roySchedId +
                ", lorange=" + lorange +
                ", hirange=" + hirange +
                ", royalty=" + royalty +
                '}';
    }

    // ================= equals =================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoySched)) return false;

        RoySched that = (RoySched) o;
        return roySchedId != null && roySchedId.equals(that.roySchedId);
    }

    // ================= hashCode =================

    @Override
    public int hashCode() {

        return roySchedId != null ? roySchedId.hashCode() : 0;
    }
}