package com.sprint.Book_Partner_Application.employee.service;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.EmployeeDTO;
import com.sprint.Book_Partner_Application.employee.dto.JobDTO;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

public interface EmployeeService {
    // Job
    JobDTO.Response createJob(JobDTO.Request request);

    List<JobDTO.Response> getAllJobs();

    JobDTO.Response getJobById(Short jobId);

    JobDTO.Response updateJob(Short jobId, JobDTO.Request request);

    // Employees
    EmployeeDTO.Response createEmployee(EmployeeDTO.Request request);

    PageResponse<EmployeeDTO.Response> getAllEmployees(String pubId, Short jobId, Pageable pageable);

    @Transactional(readOnly = true)
    PageResponse<EmployeeDTO.Response> getAllEmployees(
            String pubId, Short jobId, org.springframework.data.domain.Pageable pageable);


    EmployeeDTO.Response getEmployeeById(String empId);

    EmployeeDTO.Response updateEmployee(String empId, EmployeeDTO.UpdateRequest request);

    void deleteEmployee(String empId);

    List<EmployeeDTO.Response> getEmployeesByPartner(String pubId);
}
