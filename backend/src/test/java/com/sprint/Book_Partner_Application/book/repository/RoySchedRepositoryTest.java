package com.sprint.Book_Partner_Application.book.repository;

import com.sprint.Book_Partner_Application.book.entity.RoySched;
import com.sprint.Book_Partner_Application.book.entity.Title;
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

/**
 * Repository test class for RoySchedRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(ValidationAutoConfiguration.class)
class RoySchedRepositoryTest {

    @Autowired
    private RoySchedRepository roySchedRepository;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private Validator validator;

    private Title savedTitle;

    // ================= SETUP =================
    @BeforeEach
    void init() {
        Title title = new Title();
        title.setTitleId("T001");
        title.setTitle("Test Book");
        title.setType("business");
        title.setPubdate(LocalDateTime.now());

        savedTitle = titleRepository.saveAndFlush(title);
    }

    // ================= HELPER =================
    private RoySched createAndFlush() {
        RoySched roy = new RoySched();
        roy.setTitle(savedTitle);
        roy.setLorange(100);
        roy.setHirange(200);
        roy.setRoyalty(10);

        return roySchedRepository.saveAndFlush(roy);
    }

    // ================= CREATE =================
    @Test
    void testCreate() {
        RoySched roy = createAndFlush();

        assertNotNull(roy);
        assertNotNull(roy.getRoySchedId());
    }

    // ================= READ =================
    @Test
    void testRead() {
        RoySched roy = createAndFlush();

        RoySched found = roySchedRepository.findById(roy.getRoySchedId()).orElse(null);

        assertNotNull(found);
        assertEquals(100, found.getLorange());
        assertEquals(200, found.getHirange());
        assertEquals(10, found.getRoyalty());
    }

    // ================= UPDATE =================
    @Test
    void testUpdate() {
        RoySched roy = createAndFlush();

        roy.setRoyalty(20);
        RoySched updated = roySchedRepository.saveAndFlush(roy);

        assertEquals(20, updated.getRoyalty());
    }

    // ================= DELETE =================
    @Test
    void testDelete() {
        RoySched roy = createAndFlush();

        roySchedRepository.deleteById(roy.getRoySchedId());

        assertFalse(roySchedRepository.findById(roy.getRoySchedId()).isPresent());
    }

    // ================= FIND ALL =================
    @Test
    void testFindAll() {
        createAndFlush();

        List<RoySched> list = roySchedRepository.findAll();

        assertFalse(list.isEmpty());
    }

    // ================= FIND BY TITLE ID =================
    @Test
    void testFindByTitleTitleId() {
        createAndFlush();

        List<RoySched> list = roySchedRepository.findByTitle_TitleId("T001");

        assertFalse(list.isEmpty());
        assertEquals("T001", list.get(0).getTitle().getTitleId());
    }

    // ================= DELETE BY TITLE ID =================
    @Test
    void testDeleteByTitleTitleId() {
        createAndFlush();

        roySchedRepository.deleteByTitle_TitleId("T001");

        List<RoySched> list = roySchedRepository.findByTitle_TitleId("T001");
        assertTrue(list.isEmpty());
    }

    // ================= VALIDATION =================
    @Test
    void testValidationFail() {
        RoySched roy = new RoySched(); // missing required fields

        var violations = validator.validate(roy);

        assertFalse(violations.isEmpty());
    }
}