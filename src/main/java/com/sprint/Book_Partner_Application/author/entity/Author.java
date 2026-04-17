package com.sprint.Book_Partner_Application.author.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Data
@Builder
public class Author {

    @Id
    @Column(name = "au_id", length = 11)
    @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$", message = "Author ID must match format ###-##-####")
    private String auId;

    @Column(name = "au_lname", nullable = false, length = 40)
    @NotBlank(message = "Last name is required")
    private String auLname;

    @Column(name = "au_fname", nullable = false, length = 20)
    @NotBlank(message = "First name is required")
    private String auFname;

    @Column(name = "phone", nullable = false, length = 12)
    @NotBlank(message = "Phone is required")
    private String phone;

    @Column(name = "address", length = 40)
    private String address;

    @Column(name = "city", length = 20)
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "zip", length = 5)
    @Pattern(regexp = "^[0-9]{5}$", message = "Zip must be 5 digits")
    private String zip;

    @Column(name = "contract", nullable = false)
    private int contract;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<TitleAuthor> titleAuthors = new ArrayList<>();

    // ================= MANUAL CONSTRUCTORS =================

    // No-Args Constructor (Required by JPA)
    public Author() {
    }

    // All-Args Constructor
    public Author(String auId, String auLname, String auFname, String phone,
                  String address, String city, String state, String zip,
                  int contract, List<TitleAuthor> titleAuthors) {
        this.auId = auId;
        this.auLname = auLname;
        this.auFname = auFname;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.contract = contract;
        this.titleAuthors = titleAuthors;
    }

    // Optional: Cleaner constructor without relationships (Recommended)
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
}