package com.sprint.Book_Partner_Application.store.entity;

import com.sprint.Book_Partner_Application.sales.entity.Sale;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stores")
@Data
@Builder
public class Store {

    @Id
    @Column(name = "stor_id", length = 4)
    @NotBlank(message = "Store ID is required")
    private String storId;

    @Column(name = "stor_name", length = 40)
    private String storName;

    @Column(name = "stor_address", length = 40)
    private String storAddress;

    @Column(name = "city", length = 20)
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "zip", length = 5)
    @Pattern(regexp = "^[0-9]{5}$", message = "Zip must be 5 digits")
    private String zip;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Sale> sales = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Discount> discounts = new ArrayList<>();

    public Store() {
    }

    public Store(String storId, String storName, String storAddress, String city, String state, String zip, List<Sale> sales, List<Discount> discounts) {
        this.storId = storId;
        this.storName = storName;
        this.storAddress = storAddress;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.sales = sales;
        this.discounts = discounts;
    }
}
