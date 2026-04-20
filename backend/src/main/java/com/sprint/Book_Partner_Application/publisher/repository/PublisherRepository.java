package com.sprint.Book_Partner_Application.publisher.repository;

import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, String> {

    Page<Publisher> findByCityIgnoreCase(String city, Pageable pageable);

    Page<Publisher> findByCountryIgnoreCase(String country, Pageable pageable);

    @Query("SELECT p FROM Publisher p WHERE " +
            "(:city IS NULL OR LOWER(p.city) = LOWER(:city)) AND " +
            "(:state IS NULL OR LOWER(p.state) = LOWER(:state)) AND " +
            "(:country IS NULL OR LOWER(p.country) = LOWER(:country))")
    Page<Publisher> findWithFilters(
            @Param("city") String city,
            @Param("state") String state,
            @Param("country") String country,
            Pageable pageable);
}