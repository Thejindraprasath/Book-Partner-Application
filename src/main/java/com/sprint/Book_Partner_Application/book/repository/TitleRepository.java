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

    @Query("SELECT t FROM Title t WHERE " +
            "(:type IS NULL OR LOWER(t.type) = LOWER(:type)) AND " +
            "(:pubId IS NULL OR t.publisher.pubId = :pubId) AND " +
            "(:minPrice IS NULL OR t.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR t.price <= :maxPrice)")
    Page<Title> findWithFilters(
            @Param("type") String type,
            @Param("pubId") String pubId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);

    List<Title> findByTitleContainingIgnoreCase(String keyword);
}
