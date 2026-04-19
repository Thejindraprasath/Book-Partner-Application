package com.sprint.Book_Partner_Application.store.service;

import com.sprint.Book_Partner_Application.dto.PageResponse;

import com.sprint.Book_Partner_Application.exception.*;
import com.sprint.Book_Partner_Application.sales.dto.response.SaleResponse;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
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

        if (storeRepository.existsById(request.getStorId())) {
            throw new StoreAlreadyExistsException(request.getStorId());
        }

        if (request.getZip() != null && !request.getZip().matches("^[0-9]{5}$")) {
            throw new InvalidZipCodeException(request.getZip());
        }

        if (request.getState() != null && request.getState().length() != 2) {
            throw new InvalidStateCodeException(request.getState());
        }

        Store store = new Store();
                store.setStorId(request.getStorId());
                store.setStorName(request.getStorName());
                store.setStorAddress(request.getStorAddress());
                store.setCity(request.getCity());
                store.setState(request.getState());
                store.setZip(request.getZip());


        Store saved = storeRepository.save(store);

        return mapToResponse(saved);
    }

    @Override
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

        return mapToResponse(updated);
    }

    @Override
    public void deleteStore(String storId) {
        Store store = storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

        List<Sale> sales = saleRepository.findByStorId(storId);
        if (!sales.isEmpty()) {
            throw new StoreHasActiveSalesException(storId, sales.size());
        }

        List<Discount> discounts = discountRepository.findByStore_StorId(storId);
        if (!discounts.isEmpty()) {
            throw new StoreHasActiveDiscountsException(storId, discounts.size());
        }

        storeRepository.delete(store);
    }

    @Override
    public List<SaleResponse> getTransactionsByBranch(String storId) {
        storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

//        return saleRepository.findByStorId(storId)
//                .stream()
//                .map(s -> SaleResponse.builder()
//                        .storId(s.getStorId())
//                        .storName(s.getStore() != null ? s.getStore().getStorName() : null)
//                        .ordNum(s.getOrdNum())
//                        .ordDate(s.getOrdDate())
//                        .qty(s.getQty())
//                        .payterms(s.getPayterms())
//                        .titleId(s.getTitleId())
//                        .titleName(s.getTitle() != null ? s.getTitle().getTitle() : null)
//                        .build())
//                .collect(Collectors.toList());
        List<Sale> sales = saleRepository.findByStorId(storId);
        List<SaleResponse> responseList = new ArrayList<>();

//        for (Object obj : sales) {
//            Sale s = (Sale) obj;
        for (Sale s : sales){
            SaleResponse res = new SaleResponse();
            res.setStorId(s.getStorId());
            res.setStorName(s.getStore() != null ? s.getStore().getStorName() : null);
            res.setOrdNum(s.getOrdNum());
            res.setOrdDate(s.getOrdDate());
            res.setQty(s.getQty());
            res.setPayterms(s.getPayterms());
            res.setTitleId(s.getTitleId());
            res.setTitleName(s.getTitle() != null ? s.getTitle().getTitle() : null);

            responseList.add(res);
    }
        return responseList;
    }


    @Override
    public List<DiscountResponse> getDiscountsByBranch(String storId) {
        return getDiscountsByBranchId(storId);
    }

    // ════════════════════════════════════════════════════════
    // DISCOUNTS
    // ════════════════════════════════════════════════════════

    @Override
    public DiscountResponse createDiscount(DiscountCreateRequest request) {

        if (request.getDiscount().compareTo(BigDecimal.ZERO) < 0
                || request.getDiscount().compareTo(new BigDecimal("100.00")) > 0) {
            throw new InvalidDiscountValueException(request.getDiscount());
        }

        Store store = null;

        if (request.getStorId() != null) {
            store = storeRepository.findById(request.getStorId())
                    .orElseThrow(() -> new StoreNotFoundException(request.getStorId()));

//            boolean typeTaken = discountRepository.findByStore_StorId(request.getStorId())
//                    .stream()
//                    .anyMatch(d -> d.getDiscounttype()
//                            .equalsIgnoreCase(request.getDiscounttype()));
//
//            if (typeTaken) {
//                throw new DiscountAlreadyExistsException(
//                        request.getDiscounttype(),
//                        request.getStorId()
//                );
//            }
            List<Discount> existing = discountRepository.findByStore_StorId(request.getStorId());

            for (Discount d : existing) {
                if (d.getDiscounttype().equalsIgnoreCase(request.getDiscounttype())) {
                    throw new DiscountAlreadyExistsException(
                            request.getDiscounttype(),
                            request.getStorId()
                    );
                }
            }
        }

        Short low = request.getLowqty() != null ? request.getLowqty().shortValue() : null;
        Short high = request.getHighqty() != null ? request.getHighqty().shortValue() : null;

        if (low != null && high != null && low >= high) {
            throw new InvalidDiscountQtyRangeException(low, high);
        }

        Discount discount = new Discount();
                discount.setDiscounttype(request.getDiscounttype());
                discount.setStore(store);
                discount.setLowqty(low);
                discount.setHighqty(high);
                discount.setDiscount(request.getDiscount());

        Discount saved = discountRepository.save(discount);

        return mapDiscountToResponse(saved);
    }

    @Override
    public List<DiscountResponse> getAllDiscounts() {
//        return discountRepository.findAll()
//                .stream()
//                .map(this::mapDiscountToResponse)
//                .collect(Collectors.toList());
        List<Discount> list = discountRepository.findAll();
        List<DiscountResponse> result = new ArrayList<>();

        for (Discount d : list) {
            result.add(mapDiscountToResponse(d));
        }

        return result;
    }

    @Override
    public DiscountResponse getDiscountByType(String discountType) {
        Discount discount = discountRepository.findByDiscounttype(discountType)
                .orElseThrow(() -> new DiscountNotFoundException(discountType));

        return mapDiscountToResponse(discount);
    }

    @Override
    public List<DiscountResponse> getDiscountsByBranchId(String storId) {
        storeRepository.findById(storId)
                .orElseThrow(() -> new StoreNotFoundException(storId));

//        return discountRepository.findByStore_StorId(storId)
//                .stream()
//                .map(this::mapDiscountToResponse)
//                .collect(Collectors.toList());
        List<Discount> list = discountRepository.findByStore_StorId(storId);
        List<DiscountResponse> result = new ArrayList<>();

        for (Discount d : list) {
            result.add(mapDiscountToResponse(d));
        }

        return result;
    }

    @Override
    public DiscountResponse updateDiscount(Long discountId, DiscountUpdateRequest request) {

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

        return mapDiscountToResponse(updated);
    }

    // ════════════════════════════════════════════════════════
    // MAPPERS
    // ════════════════════════════════════════════════════════

    private StoreResponse mapToResponse(Store s) {
        StoreResponse res=new StoreResponse();
                res.setStorId(s.getStorId());
                res.setStorName(s.getStorName());
                res.setStorAddress(s.getStorAddress());
                res.setCity(s.getCity());
                res.setState(s.getState());
                res.setZip(s.getZip());
                return res;
    }

    private DiscountResponse mapDiscountToResponse(Discount d) {
        DiscountResponse res=new DiscountResponse();
                res.setDiscountId(d.getDiscountId());
                res.setDiscounttype(d.getDiscounttype());
                res.setStorId(d.getStore() != null ? d.getStore().getStorId() : null);
                res.setStorName(d.getStore() != null ? d.getStore().getStorName() : null);
                res.setLowqty(d.getLowqty() != null ? d.getLowqty().intValue() : null);
                res.setHighqty(d.getHighqty() != null ? d.getHighqty().intValue() : null);
                res.setDiscount(d.getDiscount());
                return res;
    }
}