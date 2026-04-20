package com.sprint.Book_Partner_Application.sales.controller;

import com.sprint.Book_Partner_Application.dto.ApiResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.response.SaleResponse;
import com.sprint.Book_Partner_Application.sales.dto.request.SaleCreateRequest;
import com.sprint.Book_Partner_Application.sales.service.SaleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class SaleController {

    @Autowired
    private SaleService saleService;

    // ─── CREATE ─────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<SaleResponse>> createSale(
            @Valid @RequestBody SaleCreateRequest request) {

        SaleResponse response = saleService.createSale(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sale created successfully", response));
    }

    // ─── GET ALL (PAGINATION) ───────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<SaleResponse>>> getAllSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ordDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<SaleResponse> result = saleService.getAllSales(pageable);

        return ResponseEntity.ok(
                ApiResponse.success("Sales fetched successfully", result)
        );
    }

    // ─── GET BY ID ──────────────────────────────────────
    @GetMapping("/{ordNum}")
    public ResponseEntity<ApiResponse<SaleResponse>> getSaleById(
            @PathVariable String ordNum,
            @RequestParam String storId,
            @RequestParam String titleId) {

        SaleResponse response = saleService.getSaleById(storId, ordNum, titleId);

        return ResponseEntity.ok(
                ApiResponse.success("Sale fetched successfully", response)
        );
    }

    // ─── FILTER: BY BRANCH ──────────────────────────────
    @GetMapping("/branch/{storId}")
    public ResponseEntity<ApiResponse<List<SaleResponse>>> getSalesByBranch(
            @PathVariable String storId) {

        List<SaleResponse> response = saleService.getSalesByBranch(storId);

        return ResponseEntity.ok(
                ApiResponse.success("Branch sales fetched successfully", response)
        );
    }

    // ─── FILTER: BY PRODUCT ─────────────────────────────
    @GetMapping("/product/{titleId}")
    public ResponseEntity<ApiResponse<List<SaleResponse>>> getSalesByProduct(
            @PathVariable String titleId) {

        List<SaleResponse> response = saleService.getSalesByProduct(titleId);

        return ResponseEntity.ok(
                ApiResponse.success("Product sales fetched successfully", response)
        );
    }

    // ─── FILTER: DATE RANGE ─────────────────────────────
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<SaleResponse>>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        List<SaleResponse> response = saleService.getSalesByDateRange(from, to);

        return ResponseEntity.ok(
                ApiResponse.success("Sales in date range fetched successfully", response)
        );
    }
}