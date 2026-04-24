package com.sprint.Book_Partner_Application.sales.repository;

import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
import com.sprint.Book_Partner_Application.sales.entity.Sale.SaleId;
import com.sprint.Book_Partner_Application.store.entity.Store;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(ValidationAutoConfiguration.class)
class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private Validator validator;

    // ================= SETUP =================

    @BeforeEach
    void init() {

        Store store = new Store();
        store.setStorId("S001");
        store.setStorName("Test Store");
        storeRepository.save(store);

        Title title = new Title();
        title.setTitleId("T001");
        title.setTitle("Test Book");
        title.setType("Fiction");
        title.setPubdate(LocalDateTime.now());

        titleRepository.save(title);
    }

    // ================= HELPER =================

    private Sale createSaleAndFlush() {
        Sale sale = new Sale();
        sale.setStorId("S001");
        sale.setOrdNum("O001");
        sale.setTitleId("T001");
        sale.setOrdDate(LocalDateTime.now());
        sale.setQty((short) 5);
        sale.setPayterms("CASH");

        return saleRepository.saveAndFlush(sale);
    }

    // ================= CREATE =================

    @Test
    void testCreateSale() {
        Sale sale = createSaleAndFlush();

        Sale saved = saleRepository.save(sale);

        assertNotNull(saved);
        assertEquals("S001", saved.getStorId());
    }

    // ================= READ =================

    @Test
    void testReadSale() {
        Sale sale = createSaleAndFlush();

        SaleId id = new SaleId("S001", "O001", "T001");

        Sale found = saleRepository.findById(id).orElse(null);

        assertNotNull(found);
        assertEquals("CASH", found.getPayterms());
    }

    // ================= UPDATE =================

    @Test
    void testUpdateSale() {
        Sale sale = createSaleAndFlush();

        SaleId id = new SaleId("S001", "O001", "T001");

        Sale existing = saleRepository.findById(id).get();
        existing.setPayterms("CARD");

        Sale updated = saleRepository.save(existing);

        assertEquals("CARD", updated.getPayterms());
    }

    // ================= DELETE =================

    @Test
    void testDeleteSale() {
        Sale sale = createSaleAndFlush();

        SaleId id = new SaleId("S001", "O001", "T001");

        saleRepository.deleteById(id);

        assertFalse(saleRepository.findById(id).isPresent());
    }

    // ================= FIND BY STORE =================

    @Test
    void testFindByStorId() {
        createSaleAndFlush();

        List<Sale> result = saleRepository.findByStorId("S001");

        assertFalse(result.isEmpty());
        assertEquals("S001", result.get(0).getStorId());
    }

    // ================= FIND BY TITLE =================

    @Test
    void testFindByTitleId() {
        createSaleAndFlush();

        List<Sale> result = saleRepository.findByTitleId("T001");

        assertFalse(result.isEmpty());
        assertEquals("T001", result.get(0).getTitleId());
    }

    // ================= DATE RANGE =================

    @Test
    void testFindByDateRange() {
        createSaleAndFlush();

        LocalDateTime now = LocalDateTime.now();

        List<Sale> result = saleRepository.findByDateRange(
                now.minusDays(1),
                now.plusDays(1)
        );

        assertFalse(result.isEmpty());
    }

    // ================= STORE + DATE RANGE =================

    @Test
    void testFindByStorIdAndDateRange() {
        createSaleAndFlush();

        LocalDateTime now = LocalDateTime.now();

        List<Sale> result = saleRepository.findByStorIdAndDateRange(
                "S001",
                now.minusDays(1),
                now.plusDays(1)
        );

        assertFalse(result.isEmpty());
    }

    // ================= VALIDATION =================

    @Test
    void testValidationFail() {
        Sale sale = new Sale(); // no builder

        var violations = validator.validate(sale);

        assertFalse(violations.isEmpty());
    }
}