package com.sprint.Book_Partner_Application.store.repository;

import com.sprint.Book_Partner_Application.store.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByStore_StorId(String storId);
    Optional<Discount> findByDiscounttype(String discounttype);
    List<Discount> findByStore_StorIdIsNull();

    boolean existsByStore_StorId(String storId);

    boolean existsByDiscounttypeAndStore_StorId(String discounttype, String storId);
}