package com.sprint.Book_Partner_Application.employee.repository;

import com.sprint.Book_Partner_Application.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    List<Employee> findByPublisher_PubId(String pubId);

    List<Employee> findByJob_JobId(Short jobId);

    Page<Employee> findByPublisher_PubId(String pubId, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE " +
            "(:pubId IS NULL OR e.publisher.pubId = :pubId) AND " +
            "(:jobId IS NULL OR e.job.jobId = :jobId)")
    Page<Employee> findWithFilters(
            @Param("pubId") String pubId,
            @Param("jobId") Short jobId,
            Pageable pageable);

    List<Employee> findByLnameContainingIgnoreCaseOrFnameContainingIgnoreCase(String lname, String fname);
}