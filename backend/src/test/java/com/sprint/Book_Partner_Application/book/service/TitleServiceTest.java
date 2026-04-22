package com.sprint.Book_Partner_Application.book.service;

import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.author.repository.TitleAuthorRepository;
import com.sprint.Book_Partner_Application.book.dto.request.RoySchedCreateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.RoySchedUpdateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.TitleCreateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.TitleUpdateRequest;
import com.sprint.Book_Partner_Application.book.entity.RoySched;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.exception.*;
import com.sprint.Book_Partner_Application.book.repository.RoySchedRepository;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;

import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TitleServiceImplTest {

    @InjectMocks
    private TitleServiceImpl service;

    @Mock private TitleRepository titleRepository;
    @Mock private PublisherRepository publisherRepository;
    @Mock private TitleAuthorRepository titleAuthorRepository;
    @Mock private RoySchedRepository roySchedRepository;
    @Mock private SaleRepository saleRepository;

    // ================= HELPER =================
    public Title createTitle() {
        Title t = new Title();
        t.setTitleId("T1");
        t.setTitle("Test");
        t.setType("business");
        t.setPubdate(LocalDateTime.now());
        return t;
    }

    // ================= POSITIVE =================

    @Test
    void createTitle_success() {
        TitleCreateRequest req = new TitleCreateRequest();
        req.setTitleId("T1");
        req.setTitle("Book");
        req.setType("business");
        req.setPubdate(LocalDateTime.now());

        when(titleRepository.existsById("T1")).thenReturn(false);
        when(titleRepository.save(any())).thenReturn(createTitle());

        assertNotNull(service.createTitle(req));
    }

    @Test
    void getAllTitles_success() {
        List<Title> list = List.of(createTitle());
        Page<Title> page = new PageImpl<>(list);

        when(titleRepository.findWithFilters(any(Pageable.class)))
                .thenReturn(page);

        assertNotNull(service.getAllTitles(Pageable.unpaged()));
    }

    @Test
    void getTitleById_success() {
        when(titleRepository.findById("T1"))
                .thenReturn(Optional.of(createTitle()));

        assertNotNull(service.getTitleById("T1"));
    }

    @Test
    void updateTitle_success() {
        Title title = createTitle();

        TitleUpdateRequest req = new TitleUpdateRequest();
        req.setTitle("Updated");

        when(titleRepository.findById("T1")).thenReturn(Optional.of(title));
        when(titleRepository.save(any())).thenReturn(title);

        assertNotNull(service.updateTitle("T1", req));
    }

    @Test
    void deleteTitle_success() {
        when(titleRepository.findById("T1"))
                .thenReturn(Optional.of(createTitle()));

        when(saleRepository.findByTitleId("T1"))
                .thenReturn(new ArrayList<>());

        when(titleAuthorRepository.findByTitleId("T1"))
                .thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> service.deleteTitle("T1"));
    }

    @Test
    void createRoySched_success() {
        RoySchedCreateRequest req = new RoySchedCreateRequest();
        req.setTitleId("T1");
        req.setLorange(1);
        req.setHirange(10);

        when(titleRepository.findById("T1"))
                .thenReturn(Optional.of(createTitle()));

        when(roySchedRepository.findByTitle_TitleId("T1"))
                .thenReturn(new ArrayList<>());

        when(roySchedRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertNotNull(service.createRoySched(req));
    }

    @Test
    void updateRoySched_success() {
        Title title = createTitle();

        RoySched rs = new RoySched();
        rs.setLorange(1);
        rs.setHirange(10);
        rs.setTitle(title);

        RoySchedUpdateRequest req = new RoySchedUpdateRequest();
        req.setLorange(2);
        req.setHirange(20);

        when(roySchedRepository.findById(1L))
                .thenReturn(Optional.of(rs));

        when(roySchedRepository.findByTitle_TitleId("T1"))
                .thenReturn(new ArrayList<>());

        when(roySchedRepository.save(any()))
                .thenReturn(rs);

        assertNotNull(service.updateRoySched(1L, req));
    }

    // ================= NEGATIVE =================

    @Test
    void createTitle_duplicate() {
        when(titleRepository.existsById("T1")).thenReturn(true);

        TitleCreateRequest req = new TitleCreateRequest();
        req.setTitleId("T1");

        assertThrows(TitleAlreadyExistsException.class,
                () -> service.createTitle(req));
    }

    @Test
    void createTitle_invalidType() {
        TitleCreateRequest req = new TitleCreateRequest();
        req.setTitleId("T1");
        req.setType("wrong");

        when(titleRepository.existsById("T1")).thenReturn(false);

        assertThrows(InvalidTitleTypeException.class,
                () -> service.createTitle(req));
    }

    @Test
    void createTitle_invalidPrice() {
        TitleCreateRequest req = new TitleCreateRequest();
        req.setTitleId("T1");
        req.setPrice(-10.0);

        when(titleRepository.existsById("T1")).thenReturn(false);

        assertThrows(InvalidPriceException.class,
                () -> service.createTitle(req));
    }

    @Test
    void getAllTitles_invalidRange() {
        assertThrows(RuntimeException.class,
                () -> service.getAllTitles(Pageable.unpaged()));
    }

    @Test
    void getTitle_notFound() {
        when(titleRepository.findById("T1"))
                .thenReturn(Optional.empty());

        assertThrows(TitleNotFoundException.class,
                () -> service.getTitleById("T1"));
    }

    @Test
    void deleteTitle_hasSales() {
        when(titleRepository.findById("T1"))
                .thenReturn(Optional.of(createTitle()));

        when(saleRepository.findByTitleId("T1"))
                .thenReturn(List.of(new Sale()));

        assertThrows(TitleHasActiveSalesException.class,
                () -> service.deleteTitle("T1"));
    }

    @Test
    void deleteTitle_hasAuthors() {
        when(titleRepository.findById("T1"))
                .thenReturn(Optional.of(createTitle()));

        when(saleRepository.findByTitleId("T1"))
                .thenReturn(new ArrayList<>());

        when(titleAuthorRepository.findByTitleId("T1"))
                .thenReturn(List.of(new TitleAuthor()));

        assertThrows(TitleHasActiveAuthorsException.class,
                () -> service.deleteTitle("T1"));
    }

    @Test
    void createRoySched_invalidRange() {
        RoySchedCreateRequest req = new RoySchedCreateRequest();
        req.setTitleId("T1");
        req.setLorange(10);
        req.setHirange(5);

        when(titleRepository.findById("T1"))
                .thenReturn(Optional.of(createTitle()));

        assertThrows(InvalidRoySchedRangeException.class,
                () -> service.createRoySched(req));
    }

    @Test
    void updateRoySched_notFound() {
        when(roySchedRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RoySchedNotFoundException.class,
                () -> service.updateRoySched(1L, new RoySchedUpdateRequest()));
    }
}