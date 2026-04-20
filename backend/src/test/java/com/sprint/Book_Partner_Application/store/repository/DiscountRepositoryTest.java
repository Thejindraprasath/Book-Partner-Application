package com.sprint.Book_Partner_Application.store.repository;

import com.sprint.Book_Partner_Application.store.entity.Discount;
import com.sprint.Book_Partner_Application.store.entity.Store;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DiscountRepositoryTest {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private StoreRepository storeRepository;

    // ================= HELPER METHODS =================

    private Store createStore(String id) {
        Store store = new Store();
        store.setStorId(id);
        store.setStorName("ABC Books");
        store.setCity("Chennai");
        store.setState("TN");
        store.setZip("60001");
        return store;
    }

    private Discount createDiscount(Store store, String type) {
        Discount d = new Discount();
        d.setDiscounttype(type);
        d.setStore(store);
        d.setLowqty((short) 10);
        d.setHighqty((short) 50);
        d.setDiscount(new BigDecimal("10.50"));
        return d;
    }

    // ================= CREATE =================

    @Test
    void testCreateDiscount() {
        Store store = storeRepository.save(createStore("S101"));

        Discount discount = createDiscount(store, "Festival");

        Discount saved = discountRepository.save(discount);

        assertNotNull(saved.getDiscountId());
        assertEquals("Festival", saved.getDiscounttype());
    }

    // ================= READ =================

    @Test
    void testReadDiscount() {
        Store store = storeRepository.save(createStore("S102"));
        Discount saved = discountRepository.save(createDiscount(store, "Seasonal"));

        Optional<Discount> found = discountRepository.findById(saved.getDiscountId());

        assertTrue(found.isPresent());
        assertEquals("Seasonal", found.get().getDiscounttype());
    }

    // ================= FIND BY STORE ID =================

    @Test
    void testFindByStoreId() {
        Store store = storeRepository.save(createStore("S103"));
        discountRepository.save(createDiscount(store, "Clearance"));

        List<Discount> result = discountRepository.findByStore_StorId("S103");

        assertFalse(result.isEmpty());
        assertEquals("Clearance", result.get(0).getDiscounttype());
    }

    // ================= FIND BY DISCOUNT TYPE =================

    @Test
    void testFindByDiscountType() {
        Store store = storeRepository.save(createStore("S104"));
        discountRepository.save(createDiscount(store, "NewYear"));

        Optional<Discount> result = discountRepository.findByDiscounttype("NewYear");

        assertTrue(result.isPresent());
        assertEquals("NewYear", result.get().getDiscounttype());
    }

    // ================= FIND WHERE STORE IS NULL =================

    @Test
    void testFindByStoreIsNull() {
        Discount discount = new Discount();
        discount.setDiscounttype("NoStore");
        discount.setLowqty((short) 5);
        discount.setHighqty((short) 20);
        discount.setDiscount(new BigDecimal("5.00"));

        discountRepository.save(discount);

        List<Discount> result = discountRepository.findByStore_StorIdIsNull();

        assertFalse(result.isEmpty());
    }

    // ================= UPDATE =================

    @Test
    void testUpdateDiscount() {
        Store store = storeRepository.save(createStore("S105"));
        Discount discount = discountRepository.save(createDiscount(store, "Old"));

        discount.setDiscounttype("Updated");

        Discount updated = discountRepository.save(discount);

        assertEquals("Updated", updated.getDiscounttype());
    }

    // ================= DELETE =================

    @Test
    void testDeleteDiscount() {
        Store store = storeRepository.save(createStore("S106"));
        Discount discount = discountRepository.save(createDiscount(store, "DeleteMe"));

        discountRepository.deleteById(discount.getDiscountId());

        assertFalse(discountRepository.findById(discount.getDiscountId()).isPresent());
    }

    // ================= VALIDATION TEST =================

    @Test
    void testValidationFail() {
        Discount discount = new Discount();
        discount.setDiscounttype("");   // invalid
        discount.setDiscount(null);

        assertThrows(ConstraintViolationException.class, () -> {
            discountRepository.saveAndFlush(discount);
        });
    }
}