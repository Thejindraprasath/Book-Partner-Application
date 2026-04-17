package com.sprint.Book_Partner_Application.store.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder
public class StoreCreateRequest {

    @NotBlank(message = "Store ID is required")
    private String storId;

    @NotBlank(message = "Store name is required")
    private String storName;

    @NotBlank(message = "Address is required")
    private String storAddress;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 2, message = "State must be 2 characters")
    private String state;

    @NotBlank(message = "ZIP is required")
    @Pattern(regexp = "\\d{5}", message = "ZIP must be exactly 5 digits")
    private String zip;

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

    public StoreCreateRequest() {
    }

    public StoreCreateRequest(String storId, String storName, String storAddress, String city, String state, String zip) {
        this.storId = storId;
        this.storName = storName;
        this.storAddress = storAddress;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}