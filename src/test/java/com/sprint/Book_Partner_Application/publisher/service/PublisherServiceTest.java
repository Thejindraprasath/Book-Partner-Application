package com.sprint.Book_Partner_Application.publisher.service;

import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.entity.Employee;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherCreateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherUpdateRequest;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.exception.*;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    private Publisher publisher;

    @BeforeEach
    void setup() {
        publisher = Publisher.builder()
                .pubId("1389")
                .pubName("Test Pub")
                .city("NY")
                .state("NY")
                .country("USA")
                .build();
    }

    // ================= POSITIVE =================

    @Test
    void createPublisher_success() {
        when(publisherRepository.existsById("1389")).thenReturn(false);
        when(publisherRepository.save(any())).thenReturn(publisher);

        var response = publisherService.createPublisher(
                PublisherCreateRequest.builder()
                        .pubId("1389")
                        .pubName("Test Pub")
                        .build()
        );

        assertEquals("1389", response.getPubId());
    }

    @Test
    void getPublisher_success() {
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));

        var response = publisherService.getPublisherById("1389");

        assertEquals("Test Pub", response.getPubName());
    }

    @Test
    void getAllPublishers_success() {
        Page<Publisher> page = new PageImpl<>(List.of(publisher));

        when(publisherRepository.findWithFilters(any(), any(), any(), any()))
                .thenReturn(page);

        PageResponse<?> response =
                publisherService.getAllPublishers(null, null, null, Pageable.unpaged());

        assertEquals(1, response.getTotalElements());
    }

    @Test
    void updatePublisher_success() {
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(any())).thenReturn(publisher);

        var response = publisherService.updatePublisher("1389",
                PublisherUpdateRequest.builder().pubName("Updated").build());

        assertEquals("Updated", response.getPubName());
    }

    @Test
    void deletePublisher_success() {
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(employeeRepository.findByPublisher_PubId("1389")).thenReturn(Collections.emptyList());
        when(titleRepository.findByPublisher_PubId(eq("1389"), any()))
                .thenReturn(Page.empty());

        publisherService.deletePublisher("1389");

        verify(publisherRepository).delete(publisher);
    }

    @Test
    void getEmployeesByPartner_success() {
        Employee emp = new Employee();
        emp.setEmpId("E1");
        emp.setPublisher(publisher);

        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(employeeRepository.findByPublisher_PubId("1389")).thenReturn(List.of(emp));

        var list = publisherService.getEmployeesByPartner("1389");

        assertEquals(1, list.size());
    }

    @Test
    void getProductsByPartner_success() {
        Title title = new Title();
        title.setTitleId("T1");
        title.setPublisher(publisher);

        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(titleRepository.findByPublisher_PubId(eq("1389"), any()))
                .thenReturn(new PageImpl<>(List.of(title)));

        List<TitleResponse> list = publisherService.getProductsByPartner("1389");

        assertEquals(1, list.size());
    }

    // ================= NEGATIVE =================

    @Test
    void createPublisher_duplicate() {
        when(publisherRepository.existsById("1389")).thenReturn(true);

        assertThrows(PublisherAlreadyExistsException.class,
                () -> publisherService.createPublisher(
                        PublisherCreateRequest.builder().pubId("1389").build()
                ));
    }

    @Test
    void createPublisher_invalidId() {
        when(publisherRepository.existsById("1111")).thenReturn(false);

        assertThrows(InvalidPublisherIdException.class,
                () -> publisherService.createPublisher(
                        PublisherCreateRequest.builder().pubId("1111").build()
                ));
    }

    @Test
    void getPublisher_notFound() {
        when(publisherRepository.findById("9999")).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> publisherService.getPublisherById("9999"));
    }

    @Test
    void updatePublisher_notFound() {
        when(publisherRepository.findById("9999")).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> publisherService.updatePublisher("9999", new PublisherUpdateRequest()));
    }

    @Test
    void deletePublisher_notFound() {
        when(publisherRepository.findById("9999")).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> publisherService.deletePublisher("9999"));
    }

    @Test
    void deletePublisher_hasEmployees() {
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(employeeRepository.findByPublisher_PubId("1389"))
                .thenReturn(List.of(new Employee()));

        assertThrows(PublisherHasActiveEmployeesException.class,
                () -> publisherService.deletePublisher("1389"));
    }

    @Test
    void deletePublisher_hasTitles() {
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(employeeRepository.findByPublisher_PubId("1389"))
                .thenReturn(Collections.emptyList());
        when(titleRepository.findByPublisher_PubId(eq("1389"), any()))
                .thenReturn(new PageImpl<>(List.of(new Title())));

        assertThrows(PublisherHasActiveTitlesException.class,
                () -> publisherService.deletePublisher("1389"));
    }

    @Test
    void getEmployeesByPartner_notFound() {
        when(publisherRepository.findById("9999")).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> publisherService.getEmployeesByPartner("9999"));
    }
}