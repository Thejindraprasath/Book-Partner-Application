package com.sprint.Book_Partner_Application.sales.service;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.SaleDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {
    SaleDTO.Response createSale(SaleDTO.Request request);
    PageResponse<SaleDTO.Response> getAllSales(Pageable pageable);
    SaleDTO.Response getSaleById(String storId, String ordNum, String titleId);
    List<SaleDTO.Response> getSalesByBranch(String storId);
    List<SaleDTO.Response> getSalesByProduct(String titleId);
    List<SaleDTO.Response> getSalesByDateRange(LocalDateTime from, LocalDateTime to);
}