package com.sprint.Book_Partner_Application.book.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roysched")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
