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
import com.sprint.Book_Partner_Application.store.repository.DiscountRepository;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final SaleRepository saleRepository;
    private final DiscountRepository discountRepository;

    // ---------------- STORE ----------------

    @Override
    public StoreResponse createStore(StoreCreateRequest request) {

        if (storeRepository.existsById(request.getStorId())) {
            throw new DuplicateResourceException("Store", "storId", request.getStorId());
        }

        Store store = Store.builder()
                .storId(request.getStorId())
                .storName(request.getStorName())
                .storAddress(request.getStorAddress())
                .city(request.getCity())
                .state(request.getState())
                .zip(request.getZip())
                .build();

        return mapToResponse(storeRepository.save(store));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<StoreResponse> getAllStores(String city, String state, Pageable pageable) {
        Page<Store> page = storeRepository.findWithFilters(city, state, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public StoreResponse getStoreById(String storId) {
        Store store = storeRepository.findById(storId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", storId));

        return mapToResponse(store);
    }

    @Override
    public StoreResponse updateStore(String storId, StoreUpdateRequest request) {

        Store store = storeRepository.findById(storId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", storId));

        if (request.getStorName() != null) store.setStorName(request.getStorName());
        if (request.getStorAddress() != null) store.setStorAddress(request.getStorAddress());
        if (request.getCity() != null) store.setCity(request.getCity());
        if (request.getState() != null) store.setState(request.getState());
        if (request.getZip() != null) store.setZip(request.getZip());

        return mapToResponse(storeRepository.save(store));
    }

    @Override
    public void deleteStore(String storId) {

        Store store = storeRepository.findById(storId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", storId));

        if (saleRepository.existsByStorId(storId) ||
                discountRepository.existsByStore_StorId(storId)) {

            throw new ResourceInUseException("Store", storId, "existing sales or discount records");
        }

        storeRepository.delete(store);
    }

    // ---------------- SALES ----------------

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getTransactionsByBranch(String storId) {

        storeRepository.findById(storId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", storId));

        return saleRepository.findByStorId(storId).stream()
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
                .toList();
    }

    // ---------------- DISCOUNT ----------------

    @Override
    public DiscountResponse createDiscount(DiscountCreateRequest request) {

        if (request.getLowqty() >= request.getHighqty()) {
            throw new InvalidOperationException("lowqty must be less than highqty");
        }

        Store store = storeRepository.findById(request.getStorId())
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", request.getStorId()));

        Discount discount = Discount.builder()
                .discounttype(request.getDiscounttype())
                .store(store)
                .lowqty(request.getLowqty()!= null ? request.getLowqty().shortValue() : null)
                .highqty(request.getHighqty()!= null ? request.getHighqty().shortValue() : null)
                .discount(request.getDiscount())
                .build();

        return mapDiscountToResponse(discountRepository.save(discount));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountResponse> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(this::mapDiscountToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DiscountResponse getDiscountByType(String discountType) {

        Discount discount = discountRepository.findByDiscounttype(discountType)
                .orElseThrow(() -> new ResourceNotFoundException("Discount", "type", discountType));

        return mapDiscountToResponse(discount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountResponse> getDiscountsByBranch(String storId) {
        return getDiscountsByBranchId(storId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountResponse> getDiscountsByBranchId(String storId) {

        storeRepository.findById(storId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", storId));

        return discountRepository.findByStore_StorId(storId).stream()
                .map(this::mapDiscountToResponse)
                .toList();
    }

    @Override
    public DiscountResponse updateDiscount(Long discountId, DiscountUpdateRequest request) {

        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Discount", "id", discountId));

        if (request.getDiscount() != null)
            discount.setDiscount(request.getDiscount());

        if (request.getLowqty() != null && request.getHighqty() != null &&request.getLowqty() >= request.getHighqty()) {
                throw new InvalidOperationException("lowqty must be less than highqty");
            }
            if (request.getLowqty() != null) {
                discount.setLowqty(request.getLowqty().shortValue());
            }

            if (request.getHighqty() != null) {
                discount.setHighqty(request.getHighqty().shortValue());
            }

        if (request.getDiscounttype() != null)
            discount.setDiscounttype(request.getDiscounttype());

        if (request.getStorId() != null) {
            Store store = storeRepository.findById(request.getStorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", request.getStorId()));
            discount.setStore(store);
        }

        return mapDiscountToResponse(discountRepository.save(discount));
    }

    // ---------------- MAPPERS ----------------

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