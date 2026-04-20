package com.sprint.Book_Partner_Application.store.repository;

import com.sprint.Book_Partner_Application.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

    Page<Store> findByCityIgnoreCase(String city, Pageable pageable);

    Page<Store> findByStateIgnoreCase(String state, Pageable pageable);

    @Query("SELECT s FROM Store s WHERE " +
            "(:city IS NULL OR LOWER(s.city) = LOWER(:city)) AND " +
            "(:state IS NULL OR LOWER(s.state) = LOWER(:state))")
    Page<Store> findWithFilters(
            @Param("city") String city,
            @Param("state") String state,
            Pageable pageable);
}