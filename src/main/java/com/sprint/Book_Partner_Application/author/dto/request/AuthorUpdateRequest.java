package com.sprint.Book_Partner_Application.author.dto.request;




import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public class AuthorUpdateRequest {

    private String auLname;
    private String auFname;
    private String phone;
    private String address;
    private String city;
    private String state;

    @Pattern(regexp = "^[0-9]{5}$", message = "Zip must be exactly 5 digits")
    private String zip;

    private Integer contract;

    public AuthorUpdateRequest() {}

    @Builder
    public AuthorUpdateRequest(String auLname, String auFname, String phone,
                               String address, String city, String state,
                               String zip, Integer contract) {
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