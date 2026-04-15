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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stor_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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

    // ================= COMPOSITE KEY CLASS =================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaleId implements Serializable {

        private static final long serialVersionUID = 1L;

        private String storId;
        private String ordNum;
        private String titleId;

        // IMPORTANT: Explicit equals and hashCode

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SaleId)) return false;
            SaleId that = (SaleId) o;
            return Objects.equals(storId, that.storId) &&
                    Objects.equals(ordNum, that.ordNum) &&
                    Objects.equals(titleId, that.titleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(storId, ordNum, titleId);
        }
    }
}