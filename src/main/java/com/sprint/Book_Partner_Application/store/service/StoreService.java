package com.sprint.Book_Partner_Application.store.service;

//public interface StoreService {
//}
//package com.bookpartner.service;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.store.dto.DiscountDTO;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.SaleDTO;
import com.sprint.Book_Partner_Application.store.dto.StoreDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    StoreDTO.Response createStore(StoreDTO.Request request);
    PageResponse<StoreDTO.Response> getAllStores(String city, String state, Pageable pageable);
    StoreDTO.Response getStoreById(String storId);
    StoreDTO.Response updateStore(String storId, StoreDTO.UpdateRequest request);
    void deleteStore(String storId);
    List<SaleDTO.Response> getTransactionsByBranch(String storId);
    List<DiscountDTO.Response> getDiscountsByBranch(String storId);

    // Discount operations
    DiscountDTO.Response createDiscount(DiscountDTO.Request request);
    List<DiscountDTO.Response> getAllDiscounts();
    DiscountDTO.Response getDiscountByType(String discountType);
    List<DiscountDTO.Response> getDiscountsByBranchId(String storId);
    DiscountDTO.Response updateDiscount(Long discountId, DiscountDTO.Request request);
}