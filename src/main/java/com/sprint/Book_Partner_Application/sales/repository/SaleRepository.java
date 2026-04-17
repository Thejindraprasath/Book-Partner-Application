package com.sprint.Book_Partner_Application.sales.repository;

import com.sprint.Book_Partner_Application.sales.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Sale.SaleId> {

    List<Sale> findByStorId(String storId);

    List<Sale> findByTitleId(String titleId);

    Page<Sale> findByStorId(String storId, Pageable pageable);

    @Query("SELECT s FROM Sale s WHERE s.ordDate BETWEEN :from AND :to")
    List<Sale> findByDateRange(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("SELECT s FROM Sale s WHERE s.storId = :storId AND s.ordDate BETWEEN :from AND :to")
    List<Sale> findByStorIdAndDateRange(
            @Param("storId") String storId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    boolean existsByStorId(String storId);

//    void deleteById(String id);
}
