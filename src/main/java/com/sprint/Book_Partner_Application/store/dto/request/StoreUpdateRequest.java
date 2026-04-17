package com.sprint.Book_Partner_Application.store.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
public class StoreUpdateRequest {

    private String storName;
    private String storAddress;
    private String city;
    private String state;

    @Pattern(regexp = "\\d{5}", message = "ZIP must be exactly 5 digits")
    private String zip;

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

    public StoreUpdateRequest() {
    }

    public StoreUpdateRequest(String storName, String storAddress, String city, String state, String zip) {
        this.storName = storName;
        this.storAddress = storAddress;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}