package com.sprint.Book_Partner_Application.publisher.entity;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.employee.entity.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publishers")

public class Publisher {
    @Id
    @Column(name = "pub_id", length = 4, nullable = false)
    @NotBlank(message = "Publisher ID is required")
    @Size(min = 4, max = 4, message = "Publisher ID must be exactly 4 characters")
    @Pattern(
            regexp = "^(1389|0736|0877|1622|1756|99[0-9]{2})$",
            message = "Invalid Publisher ID format"
    )
    private String pubId;

    @Column(name = "pub_name", length = 40)
    @Size(max = 40, message = "Publisher name must not exceed 40 characters")
    private String pubName;

    @Column(name = "city", length = 20)
    @Size(max = 20, message = "City must not exceed 20 characters")
    private String city;

    @Column(name = "state", length = 2)
    @Size(min = 2, max = 2, message = "State must be exactly 2 characters")
    private String state;

    @Column(name = "country", length = 30)
    @Size(max = 30, message = "Country must not exceed 30 characters")

    private String country = "USA";

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    private List<Title> titles = new ArrayList<>();

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    public Publisher() {
    }

    public Publisher(String pubId, String pubName, String city, String state, String country, List<Title> titles, List<Employee> employees) {
        this.pubId = pubId;
        this.pubName = pubName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.titles = titles;
        this.employees = employees;
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

    public List<Title> getTitles() {
        return titles;
    }

    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
