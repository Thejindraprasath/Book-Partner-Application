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

        Job job = new Job();
        job.setJobDesc("Developer");
        job.setMinLvl(50);
        job.setMaxLvl(200);

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