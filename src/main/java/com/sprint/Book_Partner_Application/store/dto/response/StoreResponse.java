package com.sprint.Book_Partner_Application.store.dto.response;

import lombok.*;

@Builder
public class StoreResponse {

    private String storId;
    private String storName;
    private String storAddress;
    private String city;
    private String state;
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

    public StoreResponse() {
    }

    public StoreResponse(String storId, String storName, String storAddress, String city, String state, String zip) {
        this.storId = storId;
        this.storName = storName;
        this.storAddress = storAddress;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}