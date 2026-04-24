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

    @Query("SELECT p FROM Publisher p")
    Page<Publisher> findWithFilters(Pageable pageable);
}