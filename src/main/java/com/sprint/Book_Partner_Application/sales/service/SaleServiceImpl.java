package com.sprint.Book_Partner_Application.sales.service;


import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.exception.TitleNotFoundException;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.response.SaleResponse;
import com.sprint.Book_Partner_Application.sales.dto.request.SaleCreateRequest;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
import com.sprint.Book_Partner_Application.sales.exception.*;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import com.sprint.Book_Partner_Application.sales.service.SaleService;
import com.sprint.Book_Partner_Application.store.entity.Store;
import com.sprint.Book_Partner_Application.store.exception.StoreNotFoundException;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
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
        log.debug("Creating sale — storId:{}, ordNum:{}, titleId:{}",
                request.getStorId(), request.getOrdNum(), request.getTitleId());

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

        if (request.getOrdDate() != null
                && request.getOrdDate().isAfter(LocalDateTime.now())) {
            throw new FutureSaleDateException(request.getOrdDate());
        }

        if (!InvalidPaytermsException.VALID_PAYTERMS.contains(request.getPayterms())) {
            throw new InvalidPaytermsException(request.getPayterms());
        }

        Sale sale = Sale.builder()
                .storId(request.getStorId())
                .ordNum(request.getOrdNum())
                .titleId(request.getTitleId())
                .ordDate(request.getOrdDate())
                .qty(request.getQty())
                .payterms(request.getPayterms())
                .build();

        Sale saved = saleRepository.save(sale);

        log.info("Sale created — storId:{}, ordNum:{}, titleId:{}",
                saved.getStorId(), saved.getOrdNum(), saved.getTitleId());

        return mapToResponse(saved, store, title);
    }

    // ─── READ ALL ─────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SaleResponse> getAllSales(Pageable pageable) {
        return PageResponse.from(
                saleRepository.findAll(pageable)
                        .map(s -> SaleResponse.builder()
                                .storId(s.getStorId())
                                .storName(s.getStore() != null ? s.getStore().getStorName() : null)
                                .ordNum(s.getOrdNum())
                                .ordDate(s.getOrdDate())
                                .qty(s.getQty())
                                .payterms(s.getPayterms())
                                .titleId(s.getTitleId())
                                .titleName(s.getTitle() != null ? s.getTitle().getTitle() : null)
                                .build())
        );
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

    // ─── BY BRANCH ─────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByBranch(String storId) {
        storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

        return saleRepository.findByStorId(storId)
                .stream()
                .map(s -> mapToResponse(s, s.getStore(), s.getTitle()))
                .collect(Collectors.toList());
    }

    // ─── BY PRODUCT ─────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByProduct(String titleId) {
        titleRepository.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));

        return saleRepository.findByTitleId(titleId)
                .stream()
                .map(s -> mapToResponse(s, s.getStore(), s.getTitle()))
                .collect(Collectors.toList());
    }

    // ─── BY DATE RANGE ─────────────────────────────────────────

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

        return saleRepository.findByDateRange(from, to)
                .stream()
                .map(s -> mapToResponse(s, s.getStore(), s.getTitle()))
                .collect(Collectors.toList());
    }

    // ─── MAPPER ─────────────────────────────────────────────

    private SaleResponse mapToResponse(Sale s, Store store, Title title) {
        return SaleResponse.builder()
                .storId(s.getStorId())
                .storName(store != null ? store.getStorName() : null)
                .ordNum(s.getOrdNum())
                .ordDate(s.getOrdDate())
                .qty(s.getQty())
                .payterms(s.getPayterms())
                .titleId(s.getTitleId())
                .titleName(title != null ? title.getTitle() : null)
                .build();
    }
}