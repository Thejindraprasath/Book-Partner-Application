package com.sprint.Book_Partner_Application.author.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AuthorCreateRequest {

    @NotBlank(message = "Author ID is required")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$",
            message = "Author ID must match format ###-##-####")
    private String auId;

    @NotBlank(message = "Last name is required")
    private String auLname;

    @NotBlank(message = "First name is required")
    private String auFname;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String address;
    private String city;
    private String state;

    @Pattern(regexp = "^[0-9]{5}$", message = "Zip must be exactly 5 digits")
    private String zip;

    @NotNull(message = "Contract is required")
    private Integer contract;

    // ✅ No-args constructor (needed for JSON deserialization)
    public AuthorCreateRequest() {}

    // ✅ All-args constructor (manual, no builder)
    public AuthorCreateRequest(String auId, String auLname, String auFname, String phone,
                               String address, String city, String state,
                               String zip, Integer contract) {
        this.auId = auId;
        this.auLname = auLname;
        this.auFname = auFname;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.contract = contract;
    }

    // Getters and Setters

    public String getAuId() { return auId; }
    public void setAuId(String auId) { this.auId = auId; }

    public String getAuLname() { return auLname; }
    public void setAuLname(String auLname) { this.auLname = auLname; }

    public String getAuFname() { return auFname; }
    public void setAuFname(String auFname) { this.auFname = auFname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    public Integer getContract() { return contract; }
    public void setContract(Integer contract) { this.contract = contract; }
}