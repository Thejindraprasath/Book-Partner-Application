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
        return Store.builder()
                .storId(id)
                .storName("ABC Books")
                .city("Chennai")
                .state("TN")
                .zip("60001")
                .build();
    }

    private Discount createDiscount(Store store, String type) {
        return Discount.builder()
                .discounttype(type)
                .store(store)
                .lowqty((short) 10)
                .highqty((short) 50)
                .discount(new BigDecimal("10.50"))
                .build();
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
        Discount discount = Discount.builder()
                .discounttype("NoStore")
                .lowqty((short) 5)
                .highqty((short) 20)
                .discount(new BigDecimal("5.00"))
                .build();

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
        Discount discount = Discount.builder()
                .discounttype("") // invalid
                .discount(null)   // invalid
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            discountRepository.saveAndFlush(discount);
        });
    }
}