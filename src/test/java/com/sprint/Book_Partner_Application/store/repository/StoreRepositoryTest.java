package com.sprint.Book_Partner_Application.store.repository;

import com.sprint.Book_Partner_Application.store.entity.Store;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    // ================= HELPER METHOD =================

    private Store createStore(String id, String city, String state) {
        return Store.builder()
                .storId(id)
                .storName("ABC Books")
                .storAddress("123 Main Street")
                .city(city)
                .state(state)
                .zip("60001")
                .build();
    }

    // ================= CREATE =================

    @Test
    void testCreateStore() {
        Store store = createStore("S001", "Chennai", "TN");

        Store saved = storeRepository.save(store);

        assertNotNull(saved);
        assertEquals("S001", saved.getStorId());
    }

    // ================= READ =================

    @Test
    void testReadStore() {
        storeRepository.save(createStore("S002", "Chennai", "TN"));

        Store found = storeRepository.findById("S002").orElse(null);

        assertNotNull(found);
        assertEquals("ABC Books", found.getStorName());
    }

    // ================= UPDATE =================

    @Test
    void testUpdateStore() {
        storeRepository.save(createStore("S003", "Chennai", "TN"));

        Store existing = storeRepository.findById("S003").orElseThrow();
        existing.setCity("Madurai");

        Store updated = storeRepository.save(existing);

        assertEquals("Madurai", updated.getCity());
    }

    // ================= DELETE =================

    @Test
    void testDeleteStore() {
        storeRepository.save(createStore("S004", "Chennai", "TN"));

        storeRepository.deleteById("S004");

        assertFalse(storeRepository.findById("S004").isPresent());
    }

    // ================= FIND BY CITY =================

    @Test
    void testFindByCity() {
        storeRepository.save(createStore("S005", "Chennai", "TN"));

        Page<Store> result = storeRepository.findByCityIgnoreCase(
                "chennai",
                PageRequest.of(0, 10)
        );

        assertFalse(result.isEmpty());
        assertEquals("Chennai", result.getContent().get(0).getCity());
    }

    // ================= FIND BY STATE =================

    @Test
    void testFindByState() {
        storeRepository.save(createStore("S006", "Chennai", "TN"));

        Page<Store> result = storeRepository.findByStateIgnoreCase(
                "tn",
                PageRequest.of(0, 10)
        );

        assertFalse(result.isEmpty());
        assertEquals("TN", result.getContent().get(0).getState());
    }

    // ================= FILTER (CITY + STATE) =================

    @Test
    void testFindWithFilters() {
        storeRepository.save(createStore("S007", "Chennai", "TN"));

        Page<Store> result = storeRepository.findWithFilters(
                "Chennai",
                "TN",
                PageRequest.of(0, 10)
        );

        assertFalse(result.isEmpty());
    }

    // ================= FILTER (ONLY CITY) =================

    @Test
    void testFindWithCityOnly() {
        storeRepository.save(createStore("S008", "Chennai", "TN"));

        Page<Store> result = storeRepository.findWithFilters(
                "Chennai",
                null,
                PageRequest.of(0, 10)
        );

        assertFalse(result.isEmpty());
    }

    // ================= FILTER (ONLY STATE) =================

    @Test
    void testFindWithStateOnly() {
        storeRepository.save(createStore("S009", "Chennai", "TN"));

        Page<Store> result = storeRepository.findWithFilters(
                null,
                "TN",
                PageRequest.of(0, 10)
        );

        assertFalse(result.isEmpty());
    }

    // ================= VALIDATION TEST =================

    @Test
    void testValidationFail() {
        Store store = Store.builder()
                .storId("")     // invalid
                .zip("123")     // invalid
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            storeRepository.saveAndFlush(store);
        });
    }
}