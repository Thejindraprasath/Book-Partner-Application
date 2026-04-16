package com.sprint.Book_Partner_Application.employee.repository;
import com.sprint.Book_Partner_Application.employee.entity.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class JobRepositoryTest {
    @Autowired
    private JobRepository jobRepository;

    @Test
    void testCreateJob() {
        Job job = Job.builder()
                .jobDesc("Developer")
                .minLvl(50)
                .maxLvl(200)
                .build();

        Job saved = jobRepository.save(job);

        assertNotNull(saved.getJobId());
    }

    @Test
    void testFindByJobDesc() {
        List<Job> jobs = jobRepository.findByJobDescContainingIgnoreCase("dev");
        assertNotNull(jobs);
    }

    @Test
    void testDeleteJob() {
        Job job = jobRepository.findAll().stream().findFirst().orElse(null);
        if (job != null) {
            jobRepository.deleteById(job.getJobId());
            assertFalse(jobRepository.findById(job.getJobId()).isPresent());
        }
    }
}
