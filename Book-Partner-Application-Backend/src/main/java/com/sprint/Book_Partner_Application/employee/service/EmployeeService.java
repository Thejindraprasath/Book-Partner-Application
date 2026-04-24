package com.sprint.Book_Partner_Application.employee.service;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.request.EmployeeCreateRequest;
import com.sprint.Book_Partner_Application.employee.dto.request.EmployeeUpdateRequest;
import com.sprint.Book_Partner_Application.employee.dto.request.JobCreateRequest;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.JobResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    // ───────────── JOB ─────────────
    JobResponse createJob(JobCreateRequest request);

    List<JobResponse> getAllJobs();

    JobResponse getJobById(Short jobId);

    JobResponse updateJob(Short jobId, JobCreateRequest request);

    // ───────────── EMPLOYEE ─────────────
    EmployeeResponse createEmployee(EmployeeCreateRequest request);

    PageResponse<EmployeeResponse> getAllEmployees( Pageable pageable);

    EmployeeResponse getEmployeeById(String empId);

    EmployeeResponse updateEmployee(String empId, EmployeeUpdateRequest request);

    void deleteEmployee(String empId);

    List<EmployeeResponse> getEmployeesByPublisher(String pubId);
}