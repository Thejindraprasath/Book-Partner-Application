package com.sprint.Book_Partner_Application.author.repository;



import com.sprint.Book_Partner_Application.author.entity.Author;
import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.author.entity.TitleAuthor.TitleAuthorId;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TitleAuthorRepositoryTest {

    @Autowired
    private TitleAuthorRepository titleAuthorRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TitleRepository titleRepository;

    @BeforeEach
    void setup() {

        // Save Author
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

        // Save Title
        titleRepository.save(Title.builder()
                .titleId("T001")
                .title("Test Book")
                .type("Fiction")
                .pubdate(LocalDateTime.now())
                .build());

        // Save Mapping
        titleAuthorRepository.save(TitleAuthor.builder()
                .auId("111-11-1111")
                .titleId("T001")
                .auOrd((short) 1)
                .royaltyper(10)
                .build());
    }

    // ================= FIND BY AUTHOR =================

    @Test
    void testFindByAuId() {
        List<TitleAuthor> result = titleAuthorRepository.findByAuId("111-11-1111");

        assertFalse(result.isEmpty());
        assertEquals("T001", result.get(0).getTitleId());
    }

    // ================= FIND BY TITLE =================

    @Test
    void testFindByTitleId() {
        List<TitleAuthor> result = titleAuthorRepository.findByTitleId("T001");

        assertFalse(result.isEmpty());
        assertEquals("111-11-1111", result.get(0).getAuId());
    }

    // ================= DELETE BY AUTHOR =================

    @Test
    void testDeleteByAuId() {
        titleAuthorRepository.deleteByAuId("111-11-1111");

        List<TitleAuthor> result = titleAuthorRepository.findByAuId("111-11-1111");

        assertTrue(result.isEmpty());
    }

    // ================= DELETE BY TITLE =================

    @Test
    void testDeleteByTitleId() {
        titleAuthorRepository.deleteByTitleId("T001");

        List<TitleAuthor> result = titleAuthorRepository.findByTitleId("T001");

        assertTrue(result.isEmpty());
    }

    // ================= FIND BY ID =================

    @Test
    void testFindById() {
        TitleAuthorId id = new TitleAuthorId("111-11-1111", "T001");

        TitleAuthor found = titleAuthorRepository.findById(id).orElse(null);

        assertNotNull(found);
    }
}