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

import org.springframework.data.domain.Pageable;

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
        job = new Job();
        job.setJobId((short) 1);
        job.setJobDesc("Developer");
        job.setMinLvl(10);
        job.setMaxLvl(100);

        publisher = new Publisher();
        publisher.setPubId("1389");
        publisher.setPubName("Test Pub");

        employee = new Employee();
        employee.setEmpId("E1");
        employee.setFname("John");
        employee.setJob(job);
        employee.setJobLvl(20);
        employee.setPublisher(publisher);
        employee.setHireDate(LocalDateTime.now());
    }

    // ================= POSITIVE =================

    @Test
    void createJob_success() {
        when(jobRepository.save(any())).thenReturn(job);

        JobCreateRequest req = new JobCreateRequest();
        req.setJobDesc("Developer");
        req.setMinLvl(10);
        req.setMaxLvl(100);

        var res = employeeService.createJob(req);

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

        JobCreateRequest req = new JobCreateRequest();
        req.setJobDesc("Updated");
        req.setMinLvl(10);
        req.setMaxLvl(120);

        var res = employeeService.updateJob((short) 1, req);

        assertEquals("Updated", res.getJobDesc());
    }

    @Test
    void createEmployee_success() {
        when(employeeRepository.existsById("E1")).thenReturn(false);
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));
        when(employeeRepository.save(any())).thenReturn(employee);

        EmployeeCreateRequest req = new EmployeeCreateRequest();
        req.setEmpId("E1");
        req.setJobId((short) 1);
        req.setJobLvl(20);
        req.setPubId("1389");

        var res = employeeService.createEmployee(req);

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

        EmployeeUpdateRequest req = new EmployeeUpdateRequest();
        req.setFname("Updated");

        var res = employeeService.updateEmployee("E1", req);

        assertEquals("Updated", res.getFname());
    }

    @Test
    void deleteEmployee_success() {
        when(employeeRepository.findById("E1")).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee("E1");

        verify(employeeRepository).delete(employee);
    }

    // ================= NEGATIVE =================

    @Test
    void createJob_invalidRange() {
        JobCreateRequest req = new JobCreateRequest();
        req.setMinLvl(100);
        req.setMaxLvl(50);

        assertThrows(InvalidJobLevelRangeException.class,
                () -> employeeService.createJob(req));
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

        JobCreateRequest req = new JobCreateRequest();
        req.setMinLvl(50);
        req.setMaxLvl(60);

        assertThrows(JobLevelUpdateBreaksEmployeesException.class,
                () -> employeeService.updateJob((short) 1, req));
    }

    @Test
    void createEmployee_duplicate() {
        when(employeeRepository.existsById("E1")).thenReturn(true);

        EmployeeCreateRequest req = new EmployeeCreateRequest();
        req.setEmpId("E1");

        assertThrows(EmployeeAlreadyExistsException.class,
                () -> employeeService.createEmployee(req));
    }

    @Test
    void createEmployee_jobNotFound() {
        when(employeeRepository.existsById("E1")).thenReturn(false);
        when(jobRepository.findById((short) 1)).thenReturn(Optional.empty());

        EmployeeCreateRequest req = new EmployeeCreateRequest();
        req.setEmpId("E1");
        req.setJobId((short) 1);

        assertThrows(JobNotFoundException.class,
                () -> employeeService.createEmployee(req));
    }

    @Test
    void createEmployee_publisherNotFound() {
        when(employeeRepository.existsById("E1")).thenReturn(false);
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));
        when(publisherRepository.findById("1389")).thenReturn(Optional.empty());

        EmployeeCreateRequest req = new EmployeeCreateRequest();
        req.setEmpId("E1");
        req.setJobId((short) 1);
        req.setPubId("1389");

        assertThrows(PublisherNotFoundException.class,
                () -> employeeService.createEmployee(req));
    }

    @Test
    void createEmployee_jobLevelMismatch() {
        when(employeeRepository.existsById("E1")).thenReturn(false);
        when(jobRepository.findById((short) 1)).thenReturn(Optional.of(job));
        when(publisherRepository.findById("1389")).thenReturn(Optional.of(publisher));

        EmployeeCreateRequest req = new EmployeeCreateRequest();
        req.setEmpId("E1");
        req.setJobId((short) 1);
        req.setJobLvl(200);
        req.setPubId("1389");

        assertThrows(EmployeeJobLevelMismatchException.class,
                () -> employeeService.createEmployee(req));
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

        EmployeeUpdateRequest req = new EmployeeUpdateRequest();
        req.setJobLvl(500);

        assertThrows(EmployeeJobLevelMismatchException.class,
                () -> employeeService.updateEmployee("E1", req));
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
                () -> employeeService.getEmployeesByPublisher("9999"));
    }

    @Test
    void getAllEmployees_invalidPublisher() {
        when(publisherRepository.existsById("9999")).thenReturn(false);

        assertThrows(PublisherNotFoundException.class,
                () -> employeeService.getAllEmployees(Pageable.unpaged()));
    }

    @Test
    void getAllEmployees_invalidJob() {
        when(publisherRepository.existsById("1389")).thenReturn(true);
        when(jobRepository.existsById((short) 5)).thenReturn(false);

        assertThrows(JobNotFoundException.class,
                () -> employeeService.getAllEmployees(Pageable.unpaged()));
    }
}