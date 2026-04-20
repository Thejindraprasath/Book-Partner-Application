package com.sprint.Book_Partner_Application.publisher.dto.request;

import jakarta.validation.constraints.NotBlank;



public class PublisherCreateRequest {

    @NotBlank(message = "Publisher ID is required")
    private String pubId;

    private String pubName;
    private String city;
    private String state;
    private String country;

    public PublisherCreateRequest(String pubId, String pubName, String city, String state, String country) {
        this.pubId = pubId;
        this.pubName = pubName;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public PublisherCreateRequest() {
    }

    public String getPubId() {
        return pubId;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
