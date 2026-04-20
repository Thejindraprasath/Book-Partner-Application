package com.sprint.Book_Partner_Application.author.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @Column(name = "au_id", length = 11)
    @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$")
    private String auId;

    @Column(name = "au_lname", nullable = false, length = 40)
    @NotBlank
    private String auLname;

    @Column(name = "au_fname", nullable = false, length = 20)
    @NotBlank
    private String auFname;

    @Column(name = "phone", nullable = false, length = 12)
    @NotBlank
    private String phone;

    @Column(name = "address", length = 40)
    private String address;

    @Column(name = "city", length = 20)
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "zip", length = 5)
    @Pattern(regexp = "^[0-9]{5}$")
    private String zip;

    @Column(name = "contract", nullable = false)
    private int contract;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<TitleAuthor> titleAuthors = new ArrayList<>();

    // ===== Constructors =====

    public Author() {}

    public Author(String auId, String auLname, String auFname, String phone,
                  String address, String city, String state, String zip,
                  int contract) {
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

    // ===== Getters & Setters =====

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

    public int getContract() { return contract; }
    public void setContract(int contract) { this.contract = contract; }

    public List<TitleAuthor> getTitleAuthors() { return titleAuthors; }
    public void setTitleAuthors(List<TitleAuthor> titleAuthors) {
        this.titleAuthors = titleAuthors;
    }
}