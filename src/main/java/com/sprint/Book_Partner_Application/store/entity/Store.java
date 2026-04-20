package com.sprint.Book_Partner_Application.store.entity;

import com.sprint.Book_Partner_Application.sales.entity.Sale;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stores")
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

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Sale> sales = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Discount> discounts = new ArrayList<>();

    public String getStorId() {
        return storId;
    }

    public void setStorId(String storId) {
        this.storId = storId;
    }

    public String getStorName() {
        return storName;
    }

    public void setStorName(String storName) {
        this.storName = storName;
    }

    public String getStorAddress() {
        return storAddress;
    }

    public void setStorAddress(String storAddress) {
        this.storAddress = storAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

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
