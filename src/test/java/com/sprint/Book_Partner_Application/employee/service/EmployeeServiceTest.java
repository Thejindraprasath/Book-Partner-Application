package com.sprint.Book_Partner_Application.employee.service;

import com.sprint.Book_Partner_Application.employee.dto.request.*;
import com.sprint.Book_Partner_Application.employee.entity.Employee;
import com.sprint.Book_Partner_Application.employee.entity.Job;
import com.sprint.Book_Partner_Application.employee.exception.*;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import com.sprint.Book_Partner_Application.employee.repository.JobRepository;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.exception.PublisherNotFoundException;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Job job;
    private Publisher publisher;
    private Employee employee;

    @BeforeEach
    void setup() {
        job = Job.builder()
                .jobId((short) 1)
                .jobDesc("Developer")
                .minLvl(10)
                .maxLvl(100)
                .build();

        publisher = Publisher.builder()
                .pubId("1389")
                .pubName("Test Pub")
                .build();

        employee = Employee.builder()
                .empId("E1")
                .fname("John")
                .job(job)
                .jobLvl(20)
                .publisher(publisher)
                .hireDate(LocalDateTime.now())
                .build();
    }

    // =====================================================
    //                POSITIVE TEST CASES
    // =====================================================

    @Test
    void createJob_success() {
        when(jobRepository.save(any())).thenReturn(job);

        var res = employeeService.createJob(
                JobCreateRequest.builder()
                        .jobDesc("Developer")
                        .minLvl(10)
                        .maxLvl(100)
                        .build()
        );

        assertEquals("Developer", res.getJobDesc());
    }

    @Test
    void getJobById_success() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));

        var res = employeeService.getJobById((short) 1);

        assertEquals("Developer", res.getJobDesc());
    }

    @Test
    void updateJob_success() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));
        when(employeeRepository.findByJob_JobId((short) 1)).thenReturn(Collections.emptyList());
        when(jobRepository.save(any())).thenReturn(job);

        var res = employeeService.updateJob((short) 1,
                JobCreateRequest.builder()
                        .jobDesc("Updated")
                        .minLvl(10)
                        .maxLvl(120)
                        .build());

        assertEquals("Updated", res.getJobDesc());
    }

    @Test
    void createEmployee_success() {
        when(employeeRepository.existsById("E1")).thenReturn(false);
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(employeeRepository.save(any())).thenReturn(employee);

        var res = employeeService.createEmployee(
                EmployeeCreateRequest.builder()
                        .empId("E1")
                        .jobId((short) 1)
                        .jobLvl(20)
                        .pubId("1389")
                        .build()
        );

        assertEquals("E1", res.getEmpId());
    }

    @Test
    void getEmployee_success() {
        when(employeeRepository.findById("E1")).thenReturn(Optional.of(employee));

        var res = employeeService.getEmployeeById("E1");

        assertEquals("E1", res.getEmpId());
    }

    @Test
    void updateEmployee_success() {
        when(employeeRepository.findById("E1")).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any())).thenReturn(employee);

        var res = employeeService.updateEmployee("E1",
                EmployeeUpdateRequest.builder().fname("Updated").build());

        assertEquals("Updated", res.getFname());
    }

    @Test
    void deleteEmployee_success() {
        when(employeeRepository.findById("E1")).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee("E1");

        verify(employeeRepository).delete(employee);
    }

    // =====================================================
    //               NEGATIVE TEST CASES
    // =====================================================

    @Test
    void createJob_invalidRange() {
        assertThrows(InvalidJobLevelRangeException.class,
                () -> employeeService.createJob(
                        JobCreateRequest.builder()
                                .minLvl(100)
                                .maxLvl(50)
                                .build()
                ));
    }

    @Test
    void getJobById_notFound() {
        when(jobRepository.findById((short) 2)).thenReturn(Optional.empty());

        assertThrows(JobNotFoundException.class,
                () -> employeeService.getJobById((short) 2));
    }

    @Test
    void updateJob_breaksEmployees() {
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));
        when(employeeRepository.findByJob_JobId((short) 1))
                .thenReturn(List.of(employee));

        assertThrows(JobLevelUpdateBreaksEmployeesException.class,
                () -> employeeService.updateJob((short) 1,
                        JobCreateRequest.builder()
                                .minLvl(50)
                                .maxLvl(60)
                                .build()));
    }

    @Test
    void createEmployee_duplicate() {
        when(employeeRepository.existsById("E1")).thenReturn(true);

        assertThrows(EmployeeAlreadyExistsException.class,
                () -> employeeService.createEmployee(
                        EmployeeCreateRequest.builder().empId("E1").build()
                ));
    }

    @Test
    void createEmployee_jobNotFound() {
        when(employeeRepository.existsById("E1")).thenReturn(false);
        when(jobRepository.findById((short) 1)).thenReturn(Optional.empty());

        assertThrows(JobNotFoundException.class,
                () -> employeeService.createEmployee(
                        EmployeeCreateRequest.builder()
                                .empId("E1")
                                .jobId((short) 1)
                                .build()
                ));
    }

    @Test
    void createEmployee_publisherNotFound() {
        when(employeeRepository.existsById("E1")).thenReturn(false);
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));
        when(publisherRepository.findById("1389")).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> employeeService.createEmployee(
                        EmployeeCreateRequest.builder()
                                .empId("E1")
                                .jobId((short) 1)
                                .pubId("1389")
                                .build()
                ));
    }

    @Test
    void createEmployee_jobLevelMismatch() {
        when(employeeRepository.existsById("E1")).thenReturn(false);
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));

        assertThrows(EmployeeJobLevelMismatchException.class,
                () -> employeeService.createEmployee(
                        EmployeeCreateRequest.builder()
                                .empId("E1")
                                .jobId((short) 1)
                                .jobLvl(200)
                                .pubId("1389")
                                .build()
                ));
    }

    @Test
    void getEmployee_notFound() {
        when(employeeRepository.findById("E2")).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById("E2"));
    }

    @Test
    void updateEmployee_jobMismatch() {
        when(employeeRepository.findById("E1")).thenReturn(Optional.of(employee));

        assertThrows(EmployeeJobLevelMismatchException.class,
                () -> employeeService.updateEmployee("E1",
                        EmployeeUpdateRequest.builder().jobLvl(500).build()));
    }

    @Test
    void deleteEmployee_notFound() {
        when(employeeRepository.findById("E2")).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee("E2"));
    }

    @Test
    void getEmployeesByPartner_notFound() {
        when(publisherRepository.findById("9999")).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> employeeService.getEmployeesByPartner("9999"));
    }

    @Test
    void getAllEmployees_invalidPublisher() {
        when(publisherRepository.existsById("9999")).thenReturn(false);

        assertThrows(PublisherNotFoundException.class,
                () -> employeeService.getAllEmployees("9999", null, Pageable.unpaged()));
    }

    @Test
    void getAllEmployees_invalidJob() {
        when(publisherRepository.existsById("1389")).thenReturn(true);
        when(jobRepository.existsById((short) 5)).thenReturn(false);

        assertThrows(JobNotFoundException.class,
                () -> employeeService.getAllEmployees("1389", (short) 5, Pageable.unpaged()));
    }
}