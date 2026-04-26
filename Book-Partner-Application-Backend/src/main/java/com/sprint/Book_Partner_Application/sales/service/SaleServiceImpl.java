package com.sprint.Book_Partner_Application.sales.service;

import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.exception.TitleNotFoundException;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.request.SaleCreateRequest;
import com.sprint.Book_Partner_Application.sales.dto.response.SaleResponse;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
import com.sprint.Book_Partner_Application.sales.exception.*;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import com.sprint.Book_Partner_Application.store.entity.Store;
import com.sprint.Book_Partner_Application.store.exception.StoreNotFoundException;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
/* Ensures all database operations in this service are executed in a single transaction.
   If any operation fails, all changes will be rolled back to maintain data consistency.
   Example: Save sale, update store, update title stock.
   If any step (like store or title update) fails,
   the entire transaction will be rolled back.
   This prevents partial data (e.g., sale saved but others failed)
   and keeps the database consistent. */
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TitleRepository titleRepository;

    // ─── CREATE ─────────────────────────────────────────────

    @Override
    public SaleResponse createSale(SaleCreateRequest request) {

        Store store = storeRepository.findById(request.getStorId())
                .orElseThrow(() -> new StoreNotFoundException(request.getStorId()));

        Title title = titleRepository.findById(request.getTitleId())
                .orElseThrow(() -> new TitleNotFoundException(request.getTitleId()));

        Sale.SaleId saleId = new Sale.SaleId(
                request.getStorId(),
                request.getOrdNum(),
                request.getTitleId()
        );

        if (saleRepository.existsById(saleId)) {
            throw new SaleAlreadyExistsException(
                    request.getStorId(),
                    request.getOrdNum(),
                    request.getTitleId()
            );
        }

        if (request.getQty() == null || request.getQty() < 1) {
            throw new InvalidSaleQuantityException(request.getQty());
        }

        if (request.getOrdDate() != null &&
                request.getOrdDate().isAfter(LocalDateTime.now())) {
            throw new FutureSaleDateException(request.getOrdDate());
        }

        if (!InvalidPaytermsException.VALID_PAYTERMS.contains(request.getPayterms())) {
            throw new InvalidPaytermsException(request.getPayterms());
        }

        // Create entity
        Sale sale = new Sale();
        sale.setStorId(request.getStorId());
        sale.setOrdNum(request.getOrdNum());
        sale.setTitleId(request.getTitleId());
        sale.setOrdDate(request.getOrdDate());
        sale.setQty(request.getQty());
        sale.setPayterms(request.getPayterms());

        Sale saved = saleRepository.save(sale);

        return mapToResponse(saved, store, title);
    }

    // ─── READ ALL (NO STREAM) ─────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SaleResponse> getAllSales(Pageable pageable) {

        Page<Sale> page = saleRepository.findAll(pageable);
        List<SaleResponse> responseList = new ArrayList<>();

        for (Sale s : page.getContent()) {
            SaleResponse response = new SaleResponse(
                    s.getStorId(),
                    s.getStore() != null ? s.getStore().getStorName() : null,
                    s.getOrdNum(),
                    s.getOrdDate(),
                    s.getQty(),
                    s.getPayterms(),
                    s.getTitleId(),
                    s.getTitle() != null ? s.getTitle().getTitle() : null
            );
            responseList.add(response);
        }

        return PageResponse.from(page, responseList);
    }

    // ─── READ ONE ─────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public SaleResponse getSaleById(String storId, String ordNum, String titleId) {

        storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

        titleRepository.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));

        Sale sale = saleRepository.findById(
                        new Sale.SaleId(storId, ordNum, titleId))
                .orElseThrow(() -> new SaleNotFoundException(storId, ordNum, titleId));

        return mapToResponse(sale, sale.getStore(), sale.getTitle());
    }

    // ─── BY BRANCH (NO STREAM) ─────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByBranch(String storId) {

        storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

        List<Sale> sales = saleRepository.findByStorId(storId);
        List<SaleResponse> responseList = new ArrayList<>();

        for (Sale s : sales) {
            responseList.add(mapToResponse(s, s.getStore(), s.getTitle()));
        }

        return responseList;
    }

    // ─── BY PRODUCT (NO STREAM) ─────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByProduct(String titleId) {

        titleRepository.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));

        List<Sale> sales = saleRepository.findByTitleId(titleId);
        List<SaleResponse> responseList = new ArrayList<>();

        for (Sale s : sales) {
            responseList.add(mapToResponse(s, s.getStore(), s.getTitle()));
        }

        return responseList;
    }

    // ─── BY DATE RANGE (NO STREAM) ─────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByDateRange(LocalDateTime from, LocalDateTime to) {

        if (from == null || to == null) {
            throw new InvalidSaleDateRangeException(
                    "Both 'from' and 'to' date parameters are required"
            );
        }

        if (from.isAfter(to)) {
            throw new InvalidSaleDateRangeException(from, to);
        }

        if (from.plusYears(5).isBefore(to)) {
            throw new InvalidSaleDateRangeException(
                    "Date range too wide — maximum allowed span is 5 years"
            );
        }

        List<Sale> sales = saleRepository.findByDateRange(from, to);
        List<SaleResponse> responseList = new ArrayList<>();

        for (Sale s : sales) {
            responseList.add(mapToResponse(s, s.getStore(), s.getTitle()));
        }

        return responseList;
    }

    // ─── MAPPER ─────────────────────────────────────────────

    private SaleResponse mapToResponse(Sale s, Store store, Title title) {

        return new SaleResponse(
                s.getStorId(),
                store != null ? store.getStorName() : null,
                s.getOrdNum(),
                s.getOrdDate(),
                s.getQty(),
                s.getPayterms(),
                s.getTitleId(),
                title != null ? title.getTitle() : null
        );
    }
}