package com.sprint.Book_Partner_Application.book.repository;

import com.sprint.Book_Partner_Application.book.entity.Title;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Repository test class for TitleRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(ValidationAutoConfiguration.class)
class TitleRepositoryTest {

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private Validator validator;

    // ================= HELPER =================
    private Title createAndFlush() {
        Title title = new Title();
        title.setTitleId("T001");
        title.setTitle("Test Book");
        title.setType("business");
        title.setPrice(100.0);
        title.setPubdate(LocalDateTime.now());
        title.setPublisher(null);

        return titleRepository.saveAndFlush(title);
    }

    // ================= CREATE =================
    @Test
    void testCreateTitle() {
        Title saved = createAndFlush();

        assertNotNull(saved);
        assertEquals("T001", saved.getTitleId());
    }

    // ================= READ =================
    @Test
    void testReadTitle() {
        createAndFlush();

        Title found = titleRepository.findById("T001").orElse(null);

        assertNotNull(found);
        assertEquals("Test Book", found.getTitle());
    }

    // ================= UPDATE =================
    @Test
    void testUpdateTitle() {
        createAndFlush();

        Title existing = titleRepository.findById("T001").orElseThrow();
        existing.setPrice(250.0);

        Title updated = titleRepository.saveAndFlush(existing);

        assertEquals(250.0, updated.getPrice());
    }

    // ================= DELETE =================
    @Test
    void testDeleteTitle() {
        createAndFlush();

        titleRepository.deleteById("T001");

        assertFalse(titleRepository.findById("T001").isPresent());
    }

    // ================= FIND ALL =================
    @Test
    void testFindAllTitles() {
        createAndFlush();

        List<Title> list = titleRepository.findAll();

        assertFalse(list.isEmpty());
    }

    // ================= FIND BY TYPE =================
    @Test
    void testFindByTypeIgnoreCase() {
        createAndFlush();

        Page<Title> result = titleRepository.findByTypeIgnoreCase(
                "BUSINESS",
                PageRequest.of(0, 10)
        );

        assertFalse(result.isEmpty());
    }

    // ================= FIND BY PRICE BETWEEN =================
    @Test
    void testFindByPriceBetween() {
        createAndFlush();

        Page<Title> result = titleRepository.findByPriceBetween(
                50.0,
                150.0,
                PageRequest.of(0, 10)
        );

        assertFalse(result.isEmpty());
    }

    // ================= FIND WITH FILTERS =================
    @Test
    void testFindWithFilters() {
        createAndFlush();

        Page<Title> result = titleRepository.findWithFilters(PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
    }

    // ================= FIND BY TITLE CONTAINING =================
    @Test
    void testFindByTitleContainingIgnoreCase() {
        createAndFlush();

        List<Title> result = titleRepository.findByTitleContainingIgnoreCase("test");

        assertFalse(result.isEmpty());
    }

    // ================= VALIDATION =================
    @Test
    void testValidationFail() {
        Title title = new Title(); // missing required fields

        var violations = validator.validate(title);

        assertFalse(violations.isEmpty());
    }
}