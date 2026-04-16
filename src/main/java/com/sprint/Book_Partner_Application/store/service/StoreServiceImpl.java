package com.sprint.Book_Partner_Application.store.service;

import com.sprint.Book_Partner_Application.store.dto.DiscountDTO;
import com.sprint.Book_Partner_Application.store.dto.PageResponse;
import com.sprint.Book_Partner_Application.sales.dto.SaleDTO;
import com.sprint.Book_Partner_Application.store.dto.StoreDTO;
import com.sprint.Book_Partner_Application.store.entity.Discount;
import com.sprint.Book_Partner_Application.store.entity.Store;
import com.sprint.Book_Partner_Application.store.exception.ResourceNotFoundException;
import com.sprint.Book_Partner_Application.store.repository.DiscountRepository;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;
import com.sprint.Book_Partner_Application.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final SaleRepository saleRepository;
    private final DiscountRepository discountRepository;

    @Override
    public StoreDTO.Response createStore(StoreDTO.Request request) {
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
    public PageResponse<StoreDTO.Response> getAllStores(String city, String state, Pageable pageable) {
        Page<Store> page = storeRepository.findWithFilters(city, state, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public StoreDTO.Response getStoreById(String storId) {
        Store store = storeRepository.findById(storId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", storId));
        return mapToResponse(store);
    }

    @Override
    public StoreDTO.Response updateStore(String storId, StoreDTO.UpdateRequest request) {
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
        storeRepository.delete(store);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleDTO.Response> getTransactionsByBranch(String storId) {
        storeRepository.findById(storId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", storId));
        return saleRepository.findByStorId(storId).stream()
                .map(s -> SaleDTO.Response.builder()
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
    public List<DiscountDTO.Response> getDiscountsByBranch(String storId) {
        return getDiscountsByBranchId(storId);
    }

    @Override
    public DiscountDTO.Response createDiscount(DiscountDTO.Request request) {
        Store store = null;
        if (request.getStorId() != null) {
            store = storeRepository.findById(request.getStorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", request.getStorId()));
        }
        Discount discount = Discount.builder()
                .discounttype(request.getDiscounttype())
                .store(store)
                .lowqty(request.getLowqty())
                .highqty(request.getHighqty())
                .discount(request.getDiscount())
                .build();
        return mapDiscountToResponse(discountRepository.save(discount));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO.Response> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(this::mapDiscountToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DiscountDTO.Response getDiscountByType(String discountType) {
        Discount discount = discountRepository.findByDiscounttype(discountType)
                .orElseThrow(() -> new ResourceNotFoundException("Discount", "type", discountType));
        return mapDiscountToResponse(discount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountDTO.Response> getDiscountsByBranchId(String storId) {
        storeRepository.findById(storId)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", storId));
        return discountRepository.findByStore_StorId(storId).stream()
                .map(this::mapDiscountToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DiscountDTO.Response updateDiscount(Long discountId, DiscountDTO.Request request) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Discount", "id", discountId));
        if (request.getDiscounttype() != null) discount.setDiscounttype(request.getDiscounttype());
        if (request.getLowqty() != null) discount.setLowqty(request.getLowqty());
        if (request.getHighqty() != null) discount.setHighqty(request.getHighqty());
        if (request.getDiscount() != null) discount.setDiscount(request.getDiscount());
        if (request.getStorId() != null) {
            Store store = storeRepository.findById(request.getStorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store", "storId", request.getStorId()));
            discount.setStore(store);
        }
        return mapDiscountToResponse(discountRepository.save(discount));
    }

    private StoreDTO.Response mapToResponse(Store s) {
        return StoreDTO.Response.builder()
                .storId(s.getStorId())
                .storName(s.getStorName())
                .storAddress(s.getStorAddress())
                .city(s.getCity())
                .state(s.getState())
                .zip(s.getZip())
                .build();
    }

    private DiscountDTO.Response mapDiscountToResponse(Discount d) {
        return DiscountDTO.Response.builder()
                .discountId(d.getDiscountId())
                .discounttype(d.getDiscounttype())
                .storId(d.getStore() != null ? d.getStore().getStorId() : null)
                .storName(d.getStore() != null ? d.getStore().getStorName() : null)
                .lowqty(d.getLowqty())
                .highqty(d.getHighqty())
                .discount(d.getDiscount())
                .build();
    }
}