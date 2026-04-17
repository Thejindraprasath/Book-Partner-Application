package com.sprint.Book_Partner_Application.store.controller;

import com.sprint.Book_Partner_Application.dto.ApiResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.request.SaleResponse;
import com.sprint.Book_Partner_Application.store.dto.request.*;
import com.sprint.Book_Partner_Application.store.dto.response.*;
import com.sprint.Book_Partner_Application.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // ================= STORES =================

    @PostMapping("/stores")
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(
            @Valid @RequestBody StoreCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Store created successfully",
                        storeService.createStore(request)
                ));
    }

    @GetMapping("/stores")
    public ResponseEntity<ApiResponse<PageResponse<StoreResponse>>> getAllStores(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
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
                        storeService.getAllStores(city, state, pageable)
                )
        );
    }

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

    @PostMapping("/discounts")
    public ResponseEntity<ApiResponse<DiscountResponse>> createDiscount(
            @Valid @RequestBody DiscountCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Discount created successfully",
                        storeService.createDiscount(request)
                ));
    }

    @GetMapping("/discounts")
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getAllDiscounts() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Discounts fetched successfully",
                        storeService.getAllDiscounts()
                )
        );
    }

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