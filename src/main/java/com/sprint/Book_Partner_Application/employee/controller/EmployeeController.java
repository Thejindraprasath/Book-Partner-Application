package com.sprint.Book_Partner_Application.employee.controller;

import com.sprint.Book_Partner_Application.dto.ApiResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.request.EmployeeCreateRequest;
import com.sprint.Book_Partner_Application.employee.dto.request.EmployeeUpdateRequest;
import com.sprint.Book_Partner_Application.employee.dto.request.JobCreateRequest;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.JobResponse;
import com.sprint.Book_Partner_Application.employee.service.EmployeeService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // ───────────── JOB APIs ─────────────

    @PostMapping("/jobs")
    public ApiResponse<JobResponse> createJob(@RequestBody JobCreateRequest request) {
        JobResponse response = employeeService.createJob(request);
        return ApiResponse.success("Job created successfully", response);
    }

    @GetMapping("/jobs")
    public ApiResponse<List<JobResponse>> getAllJobs() {
        return ApiResponse.success(employeeService.getAllJobs());
    }

    @GetMapping("/jobs/{jobId}")
    public ApiResponse<JobResponse> getJobById(@PathVariable Short jobId) {
        return ApiResponse.success(employeeService.getJobById(jobId));
    }

    @PutMapping("/jobs/{jobId}")
    public ApiResponse<JobResponse> updateJob(
            @PathVariable Short jobId,
            @RequestBody JobCreateRequest request) {

        return ApiResponse.success("Job updated successfully",
                employeeService.updateJob(jobId, request));
    }

    // ───────────── EMPLOYEE APIs ─────────────

    @PostMapping
    public ApiResponse<EmployeeResponse> createEmployee(
            @RequestBody EmployeeCreateRequest request) {

        EmployeeResponse response = employeeService.createEmployee(request);
        return ApiResponse.success("Employee created successfully", response);
    }

    @GetMapping
    public ApiResponse<PageResponse<EmployeeResponse>> getAllEmployees(
            @RequestParam(required = false) String pubId,
            @RequestParam(required = false) Short jobId,
            @PageableDefault(size = 10) Pageable pageable) {

        PageResponse<EmployeeResponse> response =
                employeeService.getAllEmployees(pubId, jobId, pageable);

        return ApiResponse.success(response);
    }

    @GetMapping("/{empId}")
    public ApiResponse<EmployeeResponse> getEmployeeById(@PathVariable String empId) {
        return ApiResponse.success(employeeService.getEmployeeById(empId));
    }

    @PutMapping("/{empId}")
    public ApiResponse<EmployeeResponse> updateEmployee(
            @PathVariable String empId,
            @RequestBody EmployeeUpdateRequest request) {

        return ApiResponse.success("Employee updated successfully",
                employeeService.updateEmployee(empId, request));
    }

    @DeleteMapping("/{empId}")
    public ApiResponse<Void> deleteEmployee(@PathVariable String empId) {
        employeeService.deleteEmployee(empId);
        return ApiResponse.successMessage("Employee deleted successfully");
    }

    // ───────────── FILTER BY PUBLISHER ─────────────

    @GetMapping("/publisher/{pubId}")
    public ApiResponse<List<EmployeeResponse>> getEmployeesByPublisher(
            @PathVariable String pubId) {

        return ApiResponse.success(employeeService.getEmployeesByPartner(pubId));
    }
}