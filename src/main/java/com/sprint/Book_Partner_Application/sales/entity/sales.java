package com.sprint.Book_Partner_Application.sales.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sales")
@IdClass(SalesId.class)
public class Sales {

    @Id
    @Column(name = "stor_id", length = 4, nullable = false)
    private String storId;

    @Id
    @Column(name = "ord_num", length = 20, nullable = false)
    private String ordNum;

    @Id
    @Column(name = "title_id", length = 10, nullable = false)
    private String titleId;

    @Column(name = "ord_date", nullable = false)
    private LocalDateTime ordDate;

    @Column(name = "qty", nullable = false)
    private Short qty;

    @Column(name = "payterms", length = 12, nullable = false)
    private String payterms;

    // 🔗 Foreign Key Mapping (Optional but recommended)
    @ManyToOne
    @JoinColumn(name = "stor_id", insertable = false, updatable = false)
    private Stores stores;

    @ManyToOne
    @JoinColumn(name = "title_id", insertable = false, updatable = false)
    private Titles titles;
}
