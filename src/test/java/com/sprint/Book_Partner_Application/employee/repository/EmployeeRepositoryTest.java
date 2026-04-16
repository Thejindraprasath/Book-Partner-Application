package com.sprint.Book_Partner_Application.employee.repository;
import com.sprint.Book_Partner_Application.employee.entity.Employee;
import com.sprint.Book_Partner_Application.employee.entity.Job;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private JobRepository jobRepository;

    @Test
    void testCreateEmployee() {

        // ✅ Create Publisher first
        Publisher publisher = Publisher.builder()
                .pubId("0736")
                .pubName("XYZ Publishers")
                .city("Chennai")
                .state("TN")
                .country("India")
                .build();

        publisherRepository.save(publisher);

        // ✅ Create Job
        Job job = Job.builder()
                .jobDesc("Tester")
                .minLvl(50)
                .maxLvl(200)
                .build();

        Job savedJob = jobRepository.save(job);

        // ✅ Create Employee
        Employee emp = Employee.builder()
                .empId("ABC12345M")
                .fname("Sanjai")
                .lname("Karthick")
                .minit("K")
                .job(savedJob)
                .jobLvl(100)
                .publisher(publisher)
                .hireDate(LocalDateTime.now())
                .build();

        Employee saved = employeeRepository.save(emp);

        assertNotNull(saved);
        assertEquals("ABC12345M", saved.getEmpId());
    }

    @Test
    void testFindByPublisher() {
        List<Employee> list = employeeRepository.findByPublisher_PubId("P002");
        assertNotNull(list);
    }

    @Test
    void testFindByJob() {
        Job job = jobRepository.findAll().stream().findFirst().orElse(null);
        if (job != null) {
            List<Employee> list = employeeRepository.findByJob_JobId(job.getJobId());
            assertNotNull(list);
        }
    }

    @Test
    void testDeleteEmployee() {
        Employee emp = employeeRepository.findAll().stream().findFirst().orElse(null);
        if (emp != null) {
            employeeRepository.deleteById(emp.getEmpId());
            assertFalse(employeeRepository.findById(emp.getEmpId()).isPresent());
        }
    }
}
