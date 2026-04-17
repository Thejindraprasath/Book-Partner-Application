package com.sprint.Book_Partner_Application.sales.service;

import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.exception.DuplicateResourceException;
import com.sprint.Book_Partner_Application.exception.InvalidOperationException;
import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;
import com.sprint.Book_Partner_Application.sales.dto.request.SaleResponse;
import com.sprint.Book_Partner_Application.sales.dto.response.SaleCreateRequest;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import com.sprint.Book_Partner_Application.store.entity.Store;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final StoreRepository storeRepository;
    private final TitleRepository titleRepository;

    // ─── CREATE ─────────────────────────────────────────
    @Override
    public SaleResponse createSale(SaleCreateRequest request) {

        Sale.SaleId id = new Sale.SaleId(
                request.getStorId(),
                request.getOrdNum(),
                request.getTitleId()
        );

        if (saleRepository.existsById(id)) {
            throw new DuplicateResourceException(
                    "Sale",
                    "composite key (storId, ordNum, titleId)",
                    id
            );
        }

        Store store = storeRepository.findById(request.getStorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Store", "storId", request.getStorId()));

        Title title = titleRepository.findById(request.getTitleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Title", "titleId", request.getTitleId()));

        // DTO → Entity
        Sale sale = Sale.builder()
                .storId(request.getStorId())
                .ordNum(request.getOrdNum())
                .titleId(request.getTitleId())
                .ordDate(request.getOrdDate())
                .qty(request.getQty())
                .payterms(request.getPayterms())
                .store(store)
                .title(title)
                .build();

        Sale saved = saleRepository.save(sale);

        return mapToResponse(saved);
    }

    // ─── GET BY ID ──────────────────────────────────────
    @Override
    public SaleResponse getSaleById(String storId, String ordNum, String titleId) {

        Sale.SaleId id = new Sale.SaleId(storId, ordNum, titleId);

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sale", "id", id));

        return mapToResponse(sale);
    }

    // ─── GET ALL ────────────────────────────────────────
    @Override
    public PageResponse<SaleResponse> getAllSales(Pageable pageable) {

        Page<Sale> page = saleRepository.findAll(pageable);

        return PageResponse.from(
                page.map(this::mapToResponse)
        );
    }

    // ─── FILTER: BRANCH ─────────────────────────────────
    @Override
    public List<SaleResponse> getSalesByBranch(String storId) {

        if (!storeRepository.existsById(storId)) {
            throw new ResourceNotFoundException("Store", "storId", storId);
        }

        return saleRepository.findByStorId(storId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ─── FILTER: PRODUCT ────────────────────────────────
    @Override
    public List<SaleResponse> getSalesByProduct(String titleId) {

        if (!titleRepository.existsById(titleId)) {
            throw new ResourceNotFoundException("Title", "titleId", titleId);
        }

        return saleRepository.findByTitleId(titleId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ─── FILTER: DATE RANGE ─────────────────────────────
    @Override
    public List<SaleResponse> getSalesByDateRange(LocalDateTime from, LocalDateTime to) {

        if (from.isAfter(to)) {
            throw new InvalidOperationException(
                    "Date range 'from' must be before 'to'"
            );
        }

        return saleRepository.findByDateRange(from, to)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ─── MAPPER ─────────────────────────────────────────
    private SaleResponse mapToResponse(Sale sale) {

        return SaleResponse.builder()
                .storId(sale.getStorId())
                .storName(sale.getStore() != null ? sale.getStore().getStorName() : null)
                .ordNum(sale.getOrdNum())
                .ordDate(sale.getOrdDate())
                .qty(sale.getQty())
                .payterms(sale.getPayterms())
                .titleId(sale.getTitleId())
                .titleName(sale.getTitle() != null ? sale.getTitle().getTitle() : null)
                .build();
    }
}