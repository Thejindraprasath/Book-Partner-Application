package com.sprint.Book_Partner_Application.store.service;

import com.sprint.Book_Partner_Application.sales.entity.Sale;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import com.sprint.Book_Partner_Application.store.dto.request.*;
import com.sprint.Book_Partner_Application.store.entity.*;
import com.sprint.Book_Partner_Application.store.exception.*;
import com.sprint.Book_Partner_Application.store.repository.DiscountRepository;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreServiceImpl service;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private DiscountRepository discountRepository;

    private Store store;

    @BeforeEach
    void setup() {
        store = Store.builder()
                .storId("S001")
                .storName("Test Store")
                .city("Chennai")
                .state("TN")
                .zip("600001")
                .build();
    }

    // ================= POSITIVE TESTS =================

    @Test
    void createStore_success() {
        StoreCreateRequest req = new StoreCreateRequest();
        req.setStorId("S001");

        when(storeRepository.existsById("S001")).thenReturn(false);
        when(storeRepository.save(any())).thenReturn(store);

        assertNotNull(service.createStore(req));
    }

    @Test
    void getStoreById_success() {
        when(storeRepository.findById("S001")).thenReturn(Optional.of(store));

        assertEquals("S001", service.getStoreById("S001").getStorId());
    }

    @Test
    void updateStore_success() {
        StoreUpdateRequest req = new StoreUpdateRequest();
        req.setCity("Madurai");

        when(storeRepository.findById("S001")).thenReturn(Optional.of(store));
        when(storeRepository.save(any())).thenReturn(store);

        assertNotNull(service.updateStore("S001", req));
    }

    @Test
    void deleteStore_success() {
        when(storeRepository.findById("S001")).thenReturn(Optional.of(store));
        when(saleRepository.findByStorId("S001")).thenReturn(Collections.emptyList());
        when(discountRepository.findByStore_StorId("S001")).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> service.deleteStore("S001"));
    }

    @Test
    void createDiscount_success() {
        DiscountCreateRequest req = new DiscountCreateRequest();
        req.setDiscounttype("Festival");
        req.setDiscount(new BigDecimal("10"));
        req.setStorId("S001");

        when(storeRepository.findById("S001")).thenReturn(Optional.of(store));
        when(discountRepository.findByStore_StorId("S001")).thenReturn(Collections.emptyList());
        when(discountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertNotNull(service.createDiscount(req));
    }

    @Test
    void getDiscountByType_success() {
        Discount d = Discount.builder().discounttype("Festival").build();

        when(discountRepository.findByDiscounttype("Festival"))
                .thenReturn(Optional.of(d));

        assertEquals("Festival", service.getDiscountByType("Festival").getDiscounttype());
    }

    @Test
    void getDiscountsByBranch_success() {
        when(storeRepository.findById("S001")).thenReturn(Optional.of(store));
        when(discountRepository.findByStore_StorId("S001"))
                .thenReturn(List.of(new Discount()));

        assertFalse(service.getDiscountsByBranch("S001").isEmpty());
    }

    @Test
    void getTransactionsByBranch_success() {
        when(storeRepository.findById("S001")).thenReturn(Optional.of(store));
        when(saleRepository.findByStorId("S001")).thenReturn(new ArrayList<>());

        assertNotNull(service.getTransactionsByBranch("S001"));
    }

    @Test
    void updateDiscount_success() {
        Discount d = Discount.builder().discountId(1L).build();

        DiscountCreateRequest req = new DiscountCreateRequest();
        req.setDiscount(new BigDecimal("20"));

        when(discountRepository.findById(1L)).thenReturn(Optional.of(d));
        when(discountRepository.save(any())).thenReturn(d);

        assertNotNull(service.updateDiscount(1L, req));
    }

    @Test
    void getAllDiscounts_success() {
        when(discountRepository.findAll()).thenReturn(List.of(new Discount()));

        assertFalse(service.getAllDiscounts().isEmpty());
    }

    // ================= NEGATIVE TESTS =================

    @Test
    void createStore_duplicate() {
        when(storeRepository.existsById("S001")).thenReturn(true);

        StoreCreateRequest req = new StoreCreateRequest();
        req.setStorId("S001");

        assertThrows(StoreAlreadyExistsException.class,
                () -> service.createStore(req));
    }

    @Test
    void createStore_invalidZip() {
        StoreCreateRequest req = new StoreCreateRequest();
        req.setStorId("S001");
        req.setZip("123");

        when(storeRepository.existsById("S001")).thenReturn(false);

        assertThrows(InvalidZipCodeException.class,
                () -> service.createStore(req));
    }

    @Test
    void createStore_invalidState() {
        StoreCreateRequest req = new StoreCreateRequest();
        req.setStorId("S001");
        req.setState("TAMIL");

        when(storeRepository.existsById("S001")).thenReturn(false);

        assertThrows(InvalidStateCodeException.class,
                () -> service.createStore(req));
    }

    @Test
    void getStore_notFound() {
        when(storeRepository.findById("S001")).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class,
                () -> service.getStoreById("S001"));
    }

    @Test
    void updateStore_notFound() {
        when(storeRepository.findById("S001")).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class,
                () -> service.updateStore("S001", new StoreUpdateRequest()));
    }

    @Test
    void deleteStore_withSales() {
        when(storeRepository.findById("S001")).thenReturn(Optional.of(store));
        when(saleRepository.findByStorId("S001")).thenReturn(List.of(new Sale()));

        assertThrows(StoreHasActiveSalesException.class,
                () -> service.deleteStore("S001"));
    }

    @Test
    void deleteStore_withDiscounts() {
        when(storeRepository.findById("S001")).thenReturn(Optional.of(store));
        when(saleRepository.findByStorId("S001")).thenReturn(Collections.emptyList());
        when(discountRepository.findByStore_StorId("S001"))
                .thenReturn(List.of(new Discount()));

        assertThrows(StoreHasActiveDiscountsException.class,
                () -> service.deleteStore("S001"));
    }

    @Test
    void createDiscount_invalidValue() {
        DiscountCreateRequest req = new DiscountCreateRequest();
        req.setDiscount(new BigDecimal("-5"));

        assertThrows(InvalidDiscountValueException.class,
                () -> service.createDiscount(req));
    }

    @Test
    void createDiscount_invalidRange() {
        DiscountCreateRequest req = new DiscountCreateRequest();
        req.setDiscount(new BigDecimal("10"));
        req.setLowqty(50);
        req.setHighqty(10);

        assertThrows(InvalidDiscountQtyRangeException.class,
                () -> service.createDiscount(req));
    }

    @Test
    void getDiscount_notFound() {
        when(discountRepository.findByDiscounttype("X"))
                .thenReturn(Optional.empty());

        assertThrows(DiscountNotFoundException.class,
                () -> service.getDiscountByType("X"));
    }
}