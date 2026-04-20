package com.sprint.Book_Partner_Application.book.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roysched")
public class RoySched {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roysched_id")
    private Long roySchedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id")
    private Title title;

    @Column(name = "lorange")
    private Integer lorange;

    @Column(name = "hirange")
    private Integer hirange;

    @Column(name = "royalty")
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
        if (!(o instanceof RoySched that)) return false;

        return roySchedId != null && roySchedId.equals(that.roySchedId);
    }

    // ================= hashCode =================

    @Override
    public int hashCode() {
        return roySchedId != null ? roySchedId.hashCode() : 0;
    }
}