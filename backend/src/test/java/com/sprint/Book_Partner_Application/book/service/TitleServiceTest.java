package com.sprint.Book_Partner_Application.book.service;

import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.author.repository.TitleAuthorRepository;
import com.sprint.Book_Partner_Application.book.dto.request.RoySchedCreateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.RoySchedUpdateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.TitleCreateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.TitleUpdateRequest;
import com.sprint.Book_Partner_Application.book.dto.response.RoySchedResponse;
import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.book.entity.RoySched;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.exception.InvalidPriceException;
import com.sprint.Book_Partner_Application.book.exception.InvalidRoySchedRangeException;
import com.sprint.Book_Partner_Application.book.exception.InvalidTitleTypeException;
import com.sprint.Book_Partner_Application.book.exception.RoySchedNotFoundException;
import com.sprint.Book_Partner_Application.book.exception.TitleAlreadyExistsException;
import com.sprint.Book_Partner_Application.book.exception.TitleHasActiveAuthorsException;
import com.sprint.Book_Partner_Application.book.exception.TitleHasActiveSalesException;
import com.sprint.Book_Partner_Application.book.exception.TitleNotFoundException;
import com.sprint.Book_Partner_Application.book.repository.RoySchedRepository;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import com.sprint.Book_Partner_Application.sales.entity.Sale;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TitleServiceImplTest {

    @InjectMocks
    private TitleServiceImpl service;

    @Mock
    private TitleRepository titleRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private TitleAuthorRepository titleAuthorRepository;

    @Mock
    private RoySchedRepository roySchedRepository;

    @Mock
    private SaleRepository saleRepository;

    // ================= HELPER =================
    private Title createTitle() {
        Title t = new Title();
        t.setTitleId("T1");
        t.setTitle("Test");
        t.setType("business");
        t.setPubdate(LocalDateTime.now());
        return t;
    }

    // ================= POSITIVE TESTS =================

    @Test
    void createTitle_success() {
        TitleCreateRequest req = new TitleCreateRequest();
        req.setTitleId("T1");
        req.setTitle("Book");
        req.setType("business");
        req.setPubdate(LocalDateTime.now());

        when(titleRepository.existsById("T1")).thenReturn(false);
        when(titleRepository.save(any(Title.class))).thenReturn(createTitle());

        TitleResponse response = service.createTitle(req);

        assertNotNull(response);
        assertEquals("T1", response.getTitleId());
        verify(titleRepository).existsById("T1");
        verify(titleRepository).save(any(Title.class));
    }

    @Test
    void getAllTitles_success() {
        List<Title> list = List.of(createTitle());
        Page<Title> page = new PageImpl<>(list);

        when(titleRepository.findWithFilters(any(Pageable.class))).thenReturn(page);

        PageResponse<TitleResponse> result = service.getAllTitles(Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getContent().size());
        verify(titleRepository).findWithFilters(any(Pageable.class));
    }

    @Test
    void getTitleById_success() {
        when(titleRepository.findById("T1")).thenReturn(Optional.of(createTitle()));

        TitleResponse response = service.getTitleById("T1");

        assertNotNull(response);
        assertEquals("T1", response.getTitleId());
        verify(titleRepository).findById("T1");
    }

    @Test
    void updateTitle_success() {
        Title title = createTitle();

        TitleUpdateRequest req = new TitleUpdateRequest();
        req.setTitle("Updated");

        when(titleRepository.findById("T1")).thenReturn(Optional.of(title));
        when(titleRepository.save(any(Title.class))).thenReturn(title);

        TitleResponse response = service.updateTitle("T1", req);

        assertNotNull(response);
        verify(titleRepository).findById("T1");
        verify(titleRepository).save(any(Title.class));
    }

    @Test
    void deleteTitle_success() {
        when(titleRepository.findById("T1")).thenReturn(Optional.of(createTitle()));
        when(saleRepository.findByTitleId("T1")).thenReturn(new ArrayList<>());
        when(titleAuthorRepository.findByTitleId("T1")).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> service.deleteTitle("T1"));

        verify(titleRepository).findById("T1");
        verify(saleRepository).findByTitleId("T1");
        verify(titleAuthorRepository).findByTitleId("T1");
        verify(titleRepository).delete(any(Title.class));
    }

    @Test
    void createRoySched_success() {
        RoySchedCreateRequest req = new RoySchedCreateRequest();
        req.setTitleId("T1");
        req.setLorange(1);
        req.setHirange(10);
        req.setRoyalty(10);

        when(titleRepository.findById("T1")).thenReturn(Optional.of(createTitle()));
        when(roySchedRepository.findByTitle_TitleId("T1")).thenReturn(new ArrayList<>());
        when(roySchedRepository.save(any(RoySched.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RoySchedResponse response = service.createRoySched(req);

        assertNotNull(response);
        verify(titleRepository).findById("T1");
        verify(roySchedRepository).findByTitle_TitleId("T1");
        verify(roySchedRepository).save(any(RoySched.class));
    }

    @Test
    void updateRoySched_success() {
        Title title = createTitle();

        RoySched rs = new RoySched();
        rs.setRoySchedId(1L);
        rs.setLorange(1);
        rs.setHirange(10);
        rs.setRoyalty(10);
        rs.setTitle(title);

        RoySchedUpdateRequest req = new RoySchedUpdateRequest();
        req.setLorange(2);
        req.setHirange(20);
        req.setRoyalty(15);

        when(roySchedRepository.findById(1L)).thenReturn(Optional.of(rs));
        when(roySchedRepository.findByTitle_TitleId("T1")).thenReturn(new ArrayList<>());
        when(roySchedRepository.save(any(RoySched.class))).thenReturn(rs);

        RoySchedResponse response = service.updateRoySched(1L, req);

        assertNotNull(response);
        verify(roySchedRepository).findById(1L);
        verify(roySchedRepository).findByTitle_TitleId("T1");
        verify(roySchedRepository).save(any(RoySched.class));
    }

    // ================= NEGATIVE TESTS =================

    @Test
    void createTitle_duplicate() {
        when(titleRepository.existsById("T1")).thenReturn(true);

        TitleCreateRequest req = new TitleCreateRequest();
        req.setTitleId("T1");

        assertThrows(TitleAlreadyExistsException.class, () -> service.createTitle(req));

        verify(titleRepository).existsById("T1");
        verify(titleRepository, never()).save(any(Title.class));
    }

    @Test
    void createTitle_invalidType() {
        TitleCreateRequest req = new TitleCreateRequest();
        req.setTitleId("T1");
        req.setType("wrong");

        when(titleRepository.existsById("T1")).thenReturn(false);

        assertThrows(InvalidTitleTypeException.class, () -> service.createTitle(req));

        verify(titleRepository).existsById("T1");
        verify(titleRepository, never()).save(any(Title.class));
    }

    @Test
    void createTitle_invalidPrice() {
        TitleCreateRequest req = new TitleCreateRequest();
        req.setTitleId("T1");
        req.setType("business");
        req.setPrice(-10.0);

        when(titleRepository.existsById("T1")).thenReturn(false);

        assertThrows(InvalidPriceException.class, () -> service.createTitle(req));

        verify(titleRepository).existsById("T1");
        verify(titleRepository, never()).save(any(Title.class));
    }

    @Test
    void getTitle_notFound() {
        when(titleRepository.findById("T1")).thenReturn(Optional.empty());

        assertThrows(TitleNotFoundException.class, () -> service.getTitleById("T1"));

        verify(titleRepository).findById("T1");
    }

    @Test
    void deleteTitle_hasSales() {
        when(titleRepository.findById("T1")).thenReturn(Optional.of(createTitle()));
        when(saleRepository.findByTitleId("T1")).thenReturn(List.of(new Sale()));

        assertThrows(TitleHasActiveSalesException.class, () -> service.deleteTitle("T1"));

        verify(titleRepository).findById("T1");
        verify(saleRepository).findByTitleId("T1");
        verify(titleRepository, never()).delete(any(Title.class));
    }

    @Test
    void deleteTitle_hasAuthors() {
        when(titleRepository.findById("T1")).thenReturn(Optional.of(createTitle()));
        when(saleRepository.findByTitleId("T1")).thenReturn(new ArrayList<>());
        when(titleAuthorRepository.findByTitleId("T1")).thenReturn(List.of(new TitleAuthor()));

        assertThrows(TitleHasActiveAuthorsException.class, () -> service.deleteTitle("T1"));

        verify(titleRepository).findById("T1");
        verify(saleRepository).findByTitleId("T1");
        verify(titleAuthorRepository).findByTitleId("T1");
        verify(titleRepository, never()).delete(any(Title.class));
    }

    @Test
    void createRoySched_invalidRange() {
        RoySchedCreateRequest req = new RoySchedCreateRequest();
        req.setTitleId("T1");
        req.setLorange(10);
        req.setHirange(5);

        when(titleRepository.findById("T1")).thenReturn(Optional.of(createTitle()));

        assertThrows(InvalidRoySchedRangeException.class, () -> service.createRoySched(req));

        verify(titleRepository).findById("T1");
        verify(roySchedRepository, never()).save(any(RoySched.class));
    }

    @Test
    void updateRoySched_notFound() {
        when(roySchedRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoySchedNotFoundException.class,
                () -> service.updateRoySched(1L, new RoySchedUpdateRequest()));

        verify(roySchedRepository).findById(1L);
        verify(roySchedRepository, never()).save(any(RoySched.class));
    }
}