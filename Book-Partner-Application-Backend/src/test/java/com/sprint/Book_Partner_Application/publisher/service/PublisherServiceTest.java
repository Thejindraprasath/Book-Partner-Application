package com.sprint.Book_Partner_Application.publisher.service;

import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.employee.entity.Employee;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherCreateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherUpdateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.response.PublisherResponse;
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
        publisher = new Publisher();
        publisher.setPubId("1389");
        publisher.setPubName("Test Pub");
        publisher.setCity("NY");
        publisher.setState("NY");
        publisher.setCountry("USA");
    }

    // ================= POSITIVE =================

    @Test
    void createPublisher_success() {
        when(publisherRepository.existsById("1389")).thenReturn(false);
        when(publisherRepository.save(any())).thenReturn(publisher);

        PublisherCreateRequest req = new PublisherCreateRequest();
        req.setPubId("1389");
        req.setPubName("Test Pub");

        PublisherResponse response = publisherService.createPublisher(req);

        assertEquals("1389", response.getPubId());
    }

    @Test
    void getPublisher_success() {
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));

        PublisherResponse response = publisherService.getPublisherById("1389");

        assertEquals("Test Pub", response.getPubName());
    }

    @Test
    void getAllPublishers_success() {

        List<Publisher> list = new ArrayList<>();
        list.add(publisher);

        Page<Publisher> page = new PageImpl<>(list);

        when(publisherRepository.findWithFilters(any()))
                .thenReturn(page);

        PageResponse<PublisherResponse> response =
                publisherService.getAllPublishers(Pageable.unpaged());

        assertEquals(1, response.getTotalElements());
    }

    @Test
    void updatePublisher_success() {
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(any())).thenReturn(publisher);

        PublisherUpdateRequest req = new PublisherUpdateRequest();
        req.setPubName("Updated");

        PublisherResponse response = publisherService.updatePublisher("1389", req);

        assertEquals("Updated", response.getPubName());
    }

    @Test
    void deletePublisher_success() {
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(employeeRepository.findByPublisher_PubId("1389")).thenReturn(new ArrayList<Employee>());
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

        List<Employee> list = new ArrayList<>();
        list.add(emp);

        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(employeeRepository.findByPublisher_PubId("1389")).thenReturn(list);

        List<EmployeeResponse> result = publisherService.getEmployeesByPublisher("1389");

        assertEquals(1, result.size());
    }

    @Test
    void getProductsByPartner_success() {

        Title title = new Title();
        title.setTitleId("T1");
        title.setPublisher(publisher);

        List<Title> list = new ArrayList<>();
        list.add(title);

        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(titleRepository.findByPublisher_PubId(eq("1389"), any()))
                .thenReturn(new PageImpl<>(list));

        List<TitleResponse> result = publisherService.getProductsByPublisher("1389");

        assertEquals(1, result.size());
    }

    // ================= NEGATIVE =================

    @Test
    void createPublisher_duplicate() {
        when(publisherRepository.existsById("1389")).thenReturn(true);

        PublisherCreateRequest req = new PublisherCreateRequest();
        req.setPubId("1389");

        assertThrows(PublisherAlreadyExistsException.class,
                () -> publisherService.createPublisher(req));
    }

    @Test
    void createPublisher_invalidId() {
        when(publisherRepository.existsById("1111")).thenReturn(false);

        PublisherCreateRequest req = new PublisherCreateRequest();
        req.setPubId("1111");

        assertThrows(InvalidPublisherIdException.class,
                () -> publisherService.createPublisher(req));
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

        List<Employee> list = new ArrayList<>();
        list.add(new Employee());

        when(employeeRepository.findByPublisher_PubId("1389"))
                .thenReturn(list);

        assertThrows(PublisherHasActiveEmployeesException.class,
                () -> publisherService.deletePublisher("1389"));
    }

    @Test
    void deletePublisher_hasTitles() {
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(employeeRepository.findByPublisher_PubId("1389"))
                .thenReturn(new ArrayList<Employee>());

        List<Title> list = new ArrayList<>();
        list.add(new Title());

        when(titleRepository.findByPublisher_PubId(eq("1389"), any()))
                .thenReturn(new PageImpl<>(list));

        assertThrows(PublisherHasActiveTitlesException.class,
                () -> publisherService.deletePublisher("1389"));
    }

    @Test
    void getEmployeesByPartner_notFound() {
        when(publisherRepository.findById("9999")).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> publisherService.getEmployeesByPublisher("9999"));
    }
}