package com.sprint.Book_Partner_Application.author.repository;



import com.sprint.Book_Partner_Application.author.entity.Author;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(ValidationAutoConfiguration.class)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private Validator validator;

    @BeforeEach
    void setup() {
        authorRepository.save(Author.builder()
                .auId("111-11-1111")
                .auFname("John")
                .auLname("Doe")
                .phone("1234567890")
                .city("Chennai")
                .state("TN")
                .zip("60001")
                .contract(1)
                .build());

        authorRepository.save(Author.builder()
                .auId("222-22-2222")
                .auFname("Jane")
                .auLname("Smith")
                .phone("9876543210")
                .city("Mumbai")
                .state("MH")
                .zip("40001")
                .contract(0)
                .build());
    }

    // ================= FIND BY CITY =================

    @Test
    void testFindByCityIgnoreCase() {
        Page<Author> result = authorRepository.findByCityIgnoreCase(
                "chennai", PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals("Chennai", result.getContent().get(0).getCity());
    }

    // ================= FIND BY STATE =================

    @Test
    void testFindByStateIgnoreCase() {
        Page<Author> result = authorRepository.findByStateIgnoreCase(
                "tn", PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals("TN", result.getContent().get(0).getState());
    }

    // ================= FIND BY CONTRACT =================

    @Test
    void testFindByContract() {
        Page<Author> result = authorRepository.findByContract(
                1, PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().get(0).getContract());
    }

    // ================= FILTER QUERY =================

    @Test
    void testFindWithFilters() {
        Page<Author> result = authorRepository.findWithFilters(
                "Chennai", "TN", 1, PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals("Chennai", result.getContent().get(0).getCity());
    }

    @Test
    void testFindWithFilters_NullParams() {
        Page<Author> result = authorRepository.findWithFilters(
                null, null, null, PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
    }

    // ================= SEARCH =================

    @Test
    void testSearchByName() {
        List<Author> result =
                authorRepository.findByAuLnameContainingIgnoreCaseOrAuFnameContainingIgnoreCase(
                        "doe", "doe");

        assertFalse(result.isEmpty());
        assertEquals("Doe", result.get(0).getAuLname());
    }

    // ================= VALIDATION =================

    @Test
    void testValidationFail() {
        Author author = new Author();

        var violations = validator.validate(author);

        assertFalse(violations.isEmpty());
    }
}