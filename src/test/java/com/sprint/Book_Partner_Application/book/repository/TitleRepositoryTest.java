package com.sprint.Book_Partner_Application.book.repository;

import com.sprint.Book_Partner_Application.book.entity.Title;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration.class)
class TitleRepositoryTest {

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private Validator validator;

    // ================= HELPER =================
    private Title createAndFlush() {
        Title title = Title.builder()
                .titleId("T001")
                .title("Test Book")
                .type("Fiction")
                .price(100.0)
                .pubdate(LocalDateTime.now())
                .publisher(null) // 🔥 IMPORTANT: avoid dependency
                .build();

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

        Title existing = titleRepository.findById("T001").get();
        existing.setPrice(250.0);

        Title updated = titleRepository.save(existing);

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

    // ================= VALIDATION =================
    @Test
    void testValidationFail() {
        Title title = new Title(); // missing required fields

        var violations = validator.validate(title);

        assertFalse(violations.isEmpty());
    }
}