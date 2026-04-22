package com.sprint.Book_Partner_Application.author.service;

import com.sprint.Book_Partner_Application.author.dto.request.AuthorCreateRequest;
import com.sprint.Book_Partner_Application.author.dto.request.AuthorUpdateRequest;
import com.sprint.Book_Partner_Application.author.entity.Author;
import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.author.repository.AuthorRepository;
import com.sprint.Book_Partner_Application.author.repository.TitleAuthorRepository;
import com.sprint.Book_Partner_Application.author.exception.*;
import com.sprint.Book_Partner_Application.exception.BusinessValidationException;
import com.sprint.Book_Partner_Application.exception.InvalidOperationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private TitleAuthorRepository titleAuthorRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= POSITIVE TEST CASES (10) =================

    @Test
    void testCreateAuthorSuccess() {
        AuthorCreateRequest req = validRequest("101-11-1111");

        when(authorRepository.existsById(req.getAuId())).thenReturn(false);
        when(authorRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertEquals(req.getAuId(), authorService.createAuthor(req).getAuId());
    }

    @Test
    void testCreateMultipleAuthors() {
        AuthorCreateRequest req1 = validRequest("102-11-1111");
        AuthorCreateRequest req2 = validRequest("103-11-1111");

        when(authorRepository.existsById(any())).thenReturn(false);
        when(authorRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertNotNull(authorService.createAuthor(req1));
        assertNotNull(authorService.createAuthor(req2));
    }

    @Test
    void testGetAuthorByIdSuccess() {
        Author author = new Author();
        author.setAuId("104-11-1111");

        when(authorRepository.findById("104-11-1111")).thenReturn(Optional.of(author));

        assertEquals("104-11-1111", authorService.getAuthorById("104-11-1111").getAuId());
    }

    @Test
    void testUpdateAuthorSuccess() {
        Author author = new Author();
        author.setAuId("105-11-1111");

        AuthorUpdateRequest req = new AuthorUpdateRequest();
        req.setCity("Chennai");

        when(authorRepository.findById("105-11-1111")).thenReturn(Optional.of(author));
        when(authorRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertEquals("Chennai", authorService.updateAuthor("105-11-1111", req).getCity());
    }

    @Test
    void testUpdateOnlyPhone() {
        Author author = new Author();
        author.setAuId("106-11-1111");

        AuthorUpdateRequest req = new AuthorUpdateRequest();
        req.setPhone("9999999999");

        when(authorRepository.findById("106-11-1111")).thenReturn(Optional.of(author));
        when(authorRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertEquals("9999999999", authorService.updateAuthor("106-11-1111", req).getPhone());
    }

    @Test
    void testDeleteAuthorSuccess() {
        when(authorRepository.findById("107")).thenReturn(Optional.of(new Author()));
        when(titleAuthorRepository.existsByAuId("107")).thenReturn(false);

        assertDoesNotThrow(() -> authorService.deleteAuthor("107"));
    }

    @Test
    void testGetProductsByAuthorSuccess() {
        when(authorRepository.findById("108")).thenReturn(Optional.of(new Author()));

        List<TitleAuthor> list = List.of(new TitleAuthor());
        when(titleAuthorRepository.findByAuId("108")).thenReturn(list);

        assertFalse(authorService.getProductsByAuthor("108").isEmpty());
    }

    @Test
    void testGetAllAuthorsSuccess() {
        when(authorRepository.findWithFilters(any(Pageable.class)))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(new Author())));

        assertNotNull(authorService.getAllAuthors(org.springframework.data.domain.PageRequest.of(0, 10)));
    }

    @Test
    void testCreateAuthorMinFields() {
        AuthorCreateRequest req = validRequest("109-11-1111");

        when(authorRepository.existsById(any())).thenReturn(false);
        when(authorRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertNotNull(authorService.createAuthor(req));
    }

    @Test
    void testUpdateZipSuccess() {
        Author author = new Author();
        author.setAuId("110");

        AuthorUpdateRequest req = new AuthorUpdateRequest();
        req.setZip("60001");

        when(authorRepository.findById("110")).thenReturn(Optional.of(author));
        when(authorRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertEquals("60001", authorService.updateAuthor("110", req).getZip());
    }

    // ================= NEGATIVE TEST CASES (10) =================

    @Test
    void testCreateDuplicateAuthor() {
        AuthorCreateRequest req = validRequest("111");

        when(authorRepository.existsById("111")).thenReturn(true);

        assertThrows(AuthorAlreadyExistsException.class,
                () -> authorService.createAuthor(req));
    }

    @Test
    void testGetAuthorNotFound() {
        when(authorRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class,
                () -> authorService.getAuthorById("999"));
    }

    @Test
    void testInvalidContractCreate() {
        AuthorCreateRequest req = validRequest("112");
        req.setContract(5);

        when(authorRepository.existsById(any())).thenReturn(false);

        assertThrows(BusinessValidationException.class,
                () -> authorService.createAuthor(req));
    }

    @Test
    void testInvalidContractFilter() {
        assertThrows(InvalidOperationException.class,
                () -> authorService.getAllAuthors(
                        org.springframework.data.domain.PageRequest.of(0, 10)));
    }

    @Test
    void testUpdateAuthorNotFound() {
        when(authorRepository.findById("113")).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class,
                () -> authorService.updateAuthor("113", new AuthorUpdateRequest()));
    }

    @Test
    void testUpdateInvalidZip() {
        Author author = new Author();
        author.setAuId("114");

        AuthorUpdateRequest req = new AuthorUpdateRequest();
        req.setZip("123");

        when(authorRepository.findById("114")).thenReturn(Optional.of(author));

        assertThrows(BusinessValidationException.class,
                () -> authorService.updateAuthor("114", req));
    }

    @Test
    void testDeleteAuthorNotFound() {
        when(authorRepository.findById("115")).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class,
                () -> authorService.deleteAuthor("115"));
    }

    @Test
    void testDeleteAuthorWithTitles() {
        when(authorRepository.findById("116")).thenReturn(Optional.of(new Author()));
        when(titleAuthorRepository.existsByAuId("116")).thenReturn(true);

        assertThrows(AuthorHasActiveTitlesException.class,
                () -> authorService.deleteAuthor("116"));
    }

    @Test
    void testGetProductsAuthorNotFound() {
        when(authorRepository.findById("117")).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class,
                () -> authorService.getProductsByAuthor("117"));
    }

    @Test
    void testGetProductsEmpty() {
        when(authorRepository.findById("118")).thenReturn(Optional.of(new Author()));
        when(titleAuthorRepository.findByAuId("118")).thenReturn(Collections.emptyList());

        assertThrows(InvalidOperationException.class,
                () -> authorService.getProductsByAuthor("118"));
    }

    // ================= HELPER =================

    private AuthorCreateRequest validRequest(String id) {
        AuthorCreateRequest req = new AuthorCreateRequest();
        req.setAuId(id);
        req.setAuFname("Test");
        req.setAuLname("User");
        req.setPhone("9999999999");
        req.setZip("60001");
        req.setContract(1);
        return req;
    }
}
