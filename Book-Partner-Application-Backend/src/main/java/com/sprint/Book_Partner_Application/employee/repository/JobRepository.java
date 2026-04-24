package com.sprint.Book_Partner_Application.employee.repository;

import com.sprint.Book_Partner_Application.employee.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Short> {
    List<Job> findByJobDescContainingIgnoreCase(String keyword);
    List<Job> findByMinLvlGreaterThanEqualAndMaxLvlLessThanEqual(int minLvl, int maxLvl);
}