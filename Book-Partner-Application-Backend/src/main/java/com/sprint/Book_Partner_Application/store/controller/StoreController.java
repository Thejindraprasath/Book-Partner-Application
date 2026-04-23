package com.sprint.Book_Partner_Application.store.controller;

import com.sprint.Book_Partner_Application.dto.ApiResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.response.SaleResponse;
import com.sprint.Book_Partner_Application.store.dto.request.*;
import com.sprint.Book_Partner_Application.store.dto.response.*;
import com.sprint.Book_Partner_Application.store.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StoreController {

    @Autowired
    private StoreService storeService;

    // ================= STORES =================
    // create store
    @PostMapping("/stores")
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(
            @Valid @RequestBody StoreCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Store created successfully",
                        storeService.createStore(request)
                ));
    }

    //get all the stores
    @GetMapping("/stores")
    public ResponseEntity<ApiResponse<PageResponse<StoreResponse>>> getAllStores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "storName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Stores fetched successfully",
                        storeService.getAllStores(pageable)
                )
        );
    }

    // get the store by using store id
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreById(
            @PathVariable String storeId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Store fetched successfully",
                        storeService.getStoreById(storeId)
                )
        );
    }

    //update the store
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponse>> updateStore(
            @PathVariable String storeId,
            @Valid @RequestBody StoreUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Store updated successfully",
                        storeService.updateStore(storeId, request)
                )
        );
    }

    //delete the store by using store id
    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponse<Void>> deleteStore(
            @PathVariable String storeId) {

        storeService.deleteStore(storeId);

        return ResponseEntity.ok(
                ApiResponse.successMessage("Store deleted successfully")
        );
    }

    @GetMapping("/stores/{storeId}/transactions")
    public ResponseEntity<ApiResponse<List<SaleResponse>>> getTransactionsByBranch(
            @PathVariable String storeId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Transactions fetched successfully",
                        storeService.getTransactionsByBranch(storeId)
                )
        );
    }

    //get the discount by using store id
    @GetMapping("/stores/{storeId}/discounts")
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getDiscountsByBranch(
            @PathVariable String storeId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Discounts fetched successfully",
                        storeService.getDiscountsByBranch(storeId)
                )
        );
    }

    // ================= DISCOUNTS =================
    //create the discount
    @PostMapping("/discounts")
    public ResponseEntity<ApiResponse<DiscountResponse>> createDiscount(
            @Valid @RequestBody DiscountCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Discount created successfully",
                        storeService.createDiscount(request)
                ));
    }

    //get all the discounts
    @GetMapping("/discounts")
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getAllDiscounts() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Discounts fetched successfully",
                        storeService.getAllDiscounts()
                )
        );
    }

    //get the discount by using discount type
    @GetMapping("/discounts/{discountType}")
    public ResponseEntity<ApiResponse<DiscountResponse>> getDiscountByType(
            @PathVariable String discountType) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Discount fetched successfully",
                        storeService.getDiscountByType(discountType)
                )
        );
    }

    //get the discounts by using store id
    @GetMapping("/discounts/branch/{storeId}")
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getDiscountsByBranchId(
            @PathVariable String storeId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Branch discounts fetched successfully",
                        storeService.getDiscountsByBranchId(storeId)
                )
        );
    }

    //update the discount
    @PutMapping("/discounts/{discountId}")
    public ResponseEntity<ApiResponse<DiscountResponse>> updateDiscount(
            @PathVariable Long discountId,
            @Valid @RequestBody DiscountUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Discount updated successfully",
                        storeService.updateDiscount(discountId, request)
                )
        );
    }
}