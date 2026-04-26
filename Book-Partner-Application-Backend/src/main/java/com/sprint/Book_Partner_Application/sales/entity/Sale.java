package com.sprint.Book_Partner_Application.sales.entity;

import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.store.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sales")
@IdClass(Sale.SaleId.class)
public class Sale {

    @Id
    @Column(name = "stor_id", length = 4)
    private String storId;

    @Id
    @Column(name = "ord_num", length = 20)
    private String ordNum;

    @Id
    @Column(name = "title_id", length = 10)
    private String titleId;

    // ================= RELATIONSHIPS =================

    @ManyToOne
    @JoinColumn(name = "stor_id", insertable = false, updatable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "title_id", insertable = false, updatable = false)
    private Title title;

    // ================= FIELDS =================

    @Column(name = "ord_date", nullable = false)
    @NotNull(message = "Order date is required")
    private LocalDateTime ordDate;

    @Column(name = "qty", nullable = false)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Short qty;

    @Column(name = "payterms", nullable = false, length = 12)
    @NotBlank(message = "Payment terms are required")
    private String payterms;

    // ================= MANUAL CONSTRUCTORS =================

    // No-Args Constructor (Required by JPA)
    public Sale() {
    }

    // All-Args Constructor
    public Sale(String storId, String ordNum, String titleId,
                Store store, Title title,
                LocalDateTime ordDate, Short qty, String payterms) {
        this.storId = storId;
        this.ordNum = ordNum;
        this.titleId = titleId;
        this.store = store;
        this.title = title;
        this.ordDate = ordDate;
        this.qty = qty;
        this.payterms = payterms;
    }

    public String getStorId() {
        return storId;
    }

    public void setStorId(String storId) {
        this.storId = storId;
    }

    public String getOrdNum() {
        return ordNum;
    }

    public void setOrdNum(String ordNum) {
        this.ordNum = ordNum;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public LocalDateTime getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(LocalDateTime ordDate) {
        this.ordDate = ordDate;
    }

    public Short getQty() {
        return qty;
    }

    public void setQty(Short qty) {
        this.qty = qty;
    }

    public String getPayterms() {
        return payterms;
    }

    public void setPayterms(String payterms) {
        this.payterms = payterms;
    }
// ================= COMPOSITE KEY CLASS =================

    public static class SaleId implements Serializable {

        //Unique Version Id used to verify the version during
        //serialization and deserialization
        private static final long serialVersionUID = 1L;

        private String storId;
        private String ordNum;
        private String titleId;

        // No-Args Constructor
        public SaleId() {
        }

        // All-Args Constructor
        public SaleId(String storId, String ordNum, String titleId) {
            this.storId = storId;
            this.ordNum = ordNum;
            this.titleId = titleId;
        }

        // Getters and Setters
        public String getStorId() {
            return storId;
        }

        public void setStorId(String storId) {
            this.storId = storId;
        }

        public String getOrdNum() {
            return ordNum;
        }

        public void setOrdNum(String ordNum) {
            this.ordNum = ordNum;
        }

        public String getTitleId() {
            return titleId;
        }

        public void setTitleId(String titleId) {
            this.titleId = titleId;
        }

        // equals()
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SaleId)) return false;
            SaleId that = (SaleId) o;
            return Objects.equals(storId, that.storId) && //Objects.equals safely handles null values
                    Objects.equals(ordNum, that.ordNum) &&
                    Objects.equals(titleId, that.titleId);
        }

        // hashCode()
        @Override
        public int hashCode() {
            return Objects.hash(storId, ordNum, titleId);
        }

        // Optional: toString()
        @Override
        public String toString() {
            return "SaleId{" +
                    "storId='" + storId + '\'' +
                    ", ordNum='" + ordNum + '\'' +
                    ", titleId='" + titleId + '\'' +
                    '}';
        }
    }
}