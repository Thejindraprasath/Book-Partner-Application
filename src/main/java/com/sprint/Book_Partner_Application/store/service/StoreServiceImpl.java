package com.sprint.Book_Partner_Application.store.service;

import com.sprint.Book_Partner_Application.dto.PageResponse;

import com.sprint.Book_Partner_Application.exception.*;
import com.sprint.Book_Partner_Application.sales.dto.response.SaleResponse;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import com.sprint.Book_Partner_Application.store.dto.request.DiscountCreateRequest;
import com.sprint.Book_Partner_Application.store.dto.request.DiscountUpdateRequest;
import com.sprint.Book_Partner_Application.store.dto.request.StoreCreateRequest;
import com.sprint.Book_Partner_Application.store.dto.request.StoreUpdateRequest;
import com.sprint.Book_Partner_Application.store.dto.response.DiscountResponse;
import com.sprint.Book_Partner_Application.store.dto.response.StoreResponse;
import com.sprint.Book_Partner_Application.store.entity.Discount;
import com.sprint.Book_Partner_Application.store.entity.Store;
import com.sprint.Book_Partner_Application.store.exception.*;
import com.sprint.Book_Partner_Application.store.repository.DiscountRepository;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class StoreServiceImpl implements StoreService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private DiscountRepository discountRepository;

    // ════════════════════════════════════════════════════════
    // STORES
    // ════════════════════════════════════════════════════════

    @Override
    public StoreResponse createStore(StoreCreateRequest request) {
        log.debug("Creating store: {}", request.getStorId());

        if (storeRepository.existsById(request.getStorId())) {
            throw new StoreAlreadyExistsException(request.getStorId());
        }

        if (request.getZip() != null && !request.getZip().matches("^[0-9]{5}$")) {
            throw new InvalidZipCodeException(request.getZip());
        }

        if (request.getState() != null && request.getState().length() != 2) {
            throw new InvalidStateCodeException(request.getState());
        }

        Store store = Store.builder()
                .storId(request.getStorId())
                .storName(request.getStorName())
                .storAddress(request.getStorAddress())
                .city(request.getCity())
                .state(request.getState())
                .zip(request.getZip())
                .build();

        Store saved = storeRepository.save(store);

        log.info("Store created: {}", saved.getStorId());
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<StoreResponse> getAllStores(String city, String state, Pageable pageable) {
        if (state != null && state.length() != 2) {
            throw new InvalidStateCodeException(state);
        }

        return PageResponse.from(
                storeRepository.findWithFilters(city, state, pageable)
                        .map(this::mapToResponse)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public StoreResponse getStoreById(String storId) {
        Store store = storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

        return mapToResponse(store);
    }

    @Override
    public StoreResponse updateStore(String storId, StoreUpdateRequest request) {
        Store store = storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

        if (request.getZip() != null && !request.getZip().matches("^[0-9]{5}$")) {
            throw new InvalidZipCodeException(request.getZip());
        }

        if (request.getState() != null && request.getState().length() != 2) {
            throw new InvalidStateCodeException(request.getState());
        }

        if (request.getStorName() != null) store.setStorName(request.getStorName());
        if (request.getStorAddress() != null) store.setStorAddress(request.getStorAddress());
        if (request.getCity() != null) store.setCity(request.getCity());
        if (request.getState() != null) store.setState(request.getState());
        if (request.getZip() != null) store.setZip(request.getZip());

        Store updated = storeRepository.save(store);

        log.info("Store updated: {}", storId);
        return mapToResponse(updated);
    }

    @Override
    public void deleteStore(String storId) {
        Store store = storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

        List<?> sales = saleRepository.findByStorId(storId);
        if (!sales.isEmpty()) {
            throw new StoreHasActiveSalesException(storId, sales.size());
        }

        List<?> discounts = discountRepository.findByStore_StorId(storId);
        if (!discounts.isEmpty()) {
            throw new StoreHasActiveDiscountsException(storId, discounts.size());
        }

        storeRepository.delete(store);

        log.info("Store deleted: {}", storId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getTransactionsByBranch(String storId) {
        storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

        return saleRepository.findByStorId(storId)
                .stream()
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
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountResponse> getDiscountsByBranch(String storId) {
        return getDiscountsByBranchId(storId);
    }

    // ════════════════════════════════════════════════════════
    // DISCOUNTS
    // ════════════════════════════════════════════════════════

    @Override
    public DiscountResponse createDiscount(DiscountCreateRequest request) {
        log.debug("Creating discount: {}", request.getDiscounttype());

        if (request.getDiscount().compareTo(BigDecimal.ZERO) < 0
                || request.getDiscount().compareTo(new BigDecimal("100.00")) > 0) {
            throw new InvalidDiscountValueException(request.getDiscount());
        }

        Store store = null;

        if (request.getStorId() != null) {
            store = storeRepository.findById(request.getStorId())
                    .orElseThrow(() -> new StoreNotFoundException(request.getStorId()));

            boolean typeTaken = discountRepository.findByStore_StorId(request.getStorId())
                    .stream()
                    .anyMatch(d -> d.getDiscounttype()
                            .equalsIgnoreCase(request.getDiscounttype()));

            if (typeTaken) {
                throw new DiscountAlreadyExistsException(
                        request.getDiscounttype(),
                        request.getStorId()
                );
            }
        }

        Short low = request.getLowqty() != null ? request.getLowqty().shortValue() : null;
        Short high = request.getHighqty() != null ? request.getHighqty().shortValue() : null;

        if (low != null && high != null && low >= high) {
            throw new InvalidDiscountQtyRangeException(low, high);
        }

        Discount discount = Discount.builder()
                .discounttype(request.getDiscounttype())
                .store(store)
                .lowqty(low)
                .highqty(high)
                .discount(request.getDiscount())
                .build();

        Discount saved = discountRepository.save(discount);

        log.info("Discount created: {}", saved.getDiscountId());
        return mapDiscountToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountResponse> getAllDiscounts() {
        return discountRepository.findAll()
                .stream()
                .map(this::mapDiscountToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DiscountResponse getDiscountByType(String discountType) {
        Discount discount = discountRepository.findByDiscounttype(discountType)
                .orElseThrow(() -> new DiscountNotFoundException(discountType));

        return mapDiscountToResponse(discount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountResponse> getDiscountsByBranchId(String storId) {
        storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

        return discountRepository.findByStore_StorId(storId)
                .stream()
                .map(this::mapDiscountToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DiscountResponse updateDiscount(Long discountId, DiscountUpdateRequest request) {
        return null;
    }

    @Override
    public DiscountResponse updateDiscount(Long discountId, DiscountCreateRequest request) {

        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new DiscountNotFoundException(discountId));

        if (request.getDiscount() != null) {
            if (request.getDiscount().compareTo(BigDecimal.ZERO) < 0
                    || request.getDiscount().compareTo(new BigDecimal("99.99")) > 0) {
                throw new InvalidDiscountValueException(request.getDiscount());
            }
        }

        Short low = request.getLowqty() != null ? request.getLowqty().shortValue() : null;
        Short high = request.getHighqty() != null ? request.getHighqty().shortValue() : null;

        if (low != null && high != null && low >= high) {
            throw new InvalidDiscountQtyRangeException(low, high);
        }

        if (request.getStorId() != null) {
            Store store = storeRepository.findById(request.getStorId())
                    .orElseThrow(() -> new StoreNotFoundException(request.getStorId()));
            discount.setStore(store);
        }

        if (request.getDiscounttype() != null) {
            discount.setDiscounttype(request.getDiscounttype());
        }

        if (low != null) {
            discount.setLowqty(low);
        }

        if (high != null) {
            discount.setHighqty(high);
        }

        if (request.getDiscount() != null) {
            discount.setDiscount(request.getDiscount());
        }

        Discount updated = discountRepository.save(discount);

        log.info("Discount updated: {}", discountId);
        return mapDiscountToResponse(updated);
    }

    // ════════════════════════════════════════════════════════
    // MAPPERS
    // ════════════════════════════════════════════════════════

    private StoreResponse mapToResponse(Store s) {
        return StoreResponse.builder()
                .storId(s.getStorId())
                .storName(s.getStorName())
                .storAddress(s.getStorAddress())
                .city(s.getCity())
                .state(s.getState())
                .zip(s.getZip())
                .build();
    }

    private DiscountResponse mapDiscountToResponse(Discount d) {
        return DiscountResponse.builder()
                .discountId(d.getDiscountId())
                .discounttype(d.getDiscounttype())
                .storId(d.getStore() != null ? d.getStore().getStorId() : null)
                .storName(d.getStore() != null ? d.getStore().getStorName() : null)
                .lowqty(d.getLowqty() != null ? d.getLowqty().intValue() : null)
                .highqty(d.getHighqty() != null ? d.getHighqty().intValue() : null)
                .discount(d.getDiscount())
                .build();
    }
}