package com.sprint.Book_Partner_Application.store.service;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.response.SaleResponse;
import com.sprint.Book_Partner_Application.store.dto.request.*;
import com.sprint.Book_Partner_Application.store.dto.response.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {

    StoreResponse createStore(StoreCreateRequest request);
    PageResponse<StoreResponse> getAllStores(String city, String state, Pageable pageable);
    StoreResponse getStoreById(String storId);
    StoreResponse updateStore(String storId, StoreUpdateRequest request);
    void deleteStore(String storId);
    List<SaleResponse> getTransactionsByBranch(String storId);
    List<DiscountResponse> getDiscountsByBranch(String storId);

    // Discount
    DiscountResponse createDiscount(DiscountCreateRequest request);
    List<DiscountResponse> getAllDiscounts();
    DiscountResponse getDiscountByType(String discountType);
    List<DiscountResponse> getDiscountsByBranchId(String storId);
    DiscountResponse updateDiscount(Long discountId, DiscountUpdateRequest request);

}