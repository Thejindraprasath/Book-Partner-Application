package com.sprint.Book_Partner_Application.book.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roysched")
@Data
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
}