package com.sprint.Book_Partner_Application.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long discountId;

    @Column(name = "discounttype", nullable = false, length = 40)
    @NotBlank(message = "Discount type is required")
    private String discounttype;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stor_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Store store;

    @Column(name = "lowqty")
    private Short lowqty;

    @Column(name = "highqty")
    private Short highqty;

    @Column(name = "discount", nullable = false, precision = 4, scale = 2)
    @NotNull(message = "Discount value is required")
    private BigDecimal discount;
}