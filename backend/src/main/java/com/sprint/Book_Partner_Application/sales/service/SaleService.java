package com.sprint.Book_Partner_Application.sales.service;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.response.SaleResponse;
import com.sprint.Book_Partner_Application.sales.dto.request.SaleCreateRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {

    SaleResponse createSale(SaleCreateRequest request);

    PageResponse<SaleResponse> getAllSales(Pageable pageable);

    SaleResponse getSaleById(String storId, String ordNum, String titleId);

    List<SaleResponse> getSalesByBranch(String storId);

    List<SaleResponse> getSalesByProduct(String titleId);

    List<SaleResponse> getSalesByDateRange(LocalDateTime from, LocalDateTime to);
}