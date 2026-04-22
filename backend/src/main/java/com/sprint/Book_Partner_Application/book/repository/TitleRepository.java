package com.sprint.Book_Partner_Application.book.repository;

import com.sprint.Book_Partner_Application.book.entity.Title;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitleRepository extends JpaRepository<Title, String> {

    Page<Title> findByTypeIgnoreCase(String type, Pageable pageable);

    Page<Title> findByPublisher_PubId(String pubId, Pageable pageable);

    Page<Title> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

    @Query("SELECT t FROM Title t")
    Page<Title> findWithFilters(
            Pageable pageable);

    List<Title> findByTitleContainingIgnoreCase(String keyword);

    boolean existsByPublisher_PubId(String pubId);
}
