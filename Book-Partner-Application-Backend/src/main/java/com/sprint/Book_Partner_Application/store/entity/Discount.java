package com.sprint.Book_Partner_Application.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long discountId;

    @Column(name = "discounttype", nullable = false, length = 40)
    @NotBlank(message = "Discount type is required")
    private String discounttype;

    @ManyToOne
    @JoinColumn(name = "stor_id")
    private Store store;

    @Column(name = "lowqty")
    private Short lowqty;

    @Column(name = "highqty")
    private Short highqty;

    @Column(name = "discount", nullable = false, precision = 4, scale = 2)
    @NotNull(message = "Discount value is required")
    private BigDecimal discount;

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public String getDiscounttype() {
        return discounttype;
    }

    public void setDiscounttype(String discounttype) {
        this.discounttype = discounttype;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Short getLowqty() {
        return lowqty;
    }

    public void setLowqty(Short lowqty) {
        this.lowqty = lowqty;
    }

    public Short getHighqty() {
        return highqty;
    }

    public void setHighqty(Short highqty) {
        this.highqty = highqty;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Discount() {
    }

    public Discount(Long discountId, String discounttype, Store store, Short lowqty, Short highqty, BigDecimal discount) {
        this.discountId = discountId;
        this.discounttype = discounttype;
        this.store = store;
        this.lowqty = lowqty;
        this.highqty = highqty;
        this.discount = discount;
    }
}