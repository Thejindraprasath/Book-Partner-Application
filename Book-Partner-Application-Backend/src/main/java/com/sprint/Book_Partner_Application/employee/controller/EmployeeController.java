package com.sprint.Book_Partner_Application.employee.controller;

import com.sprint.Book_Partner_Application.dto.ApiResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.request.EmployeeCreateRequest;
import com.sprint.Book_Partner_Application.employee.dto.request.EmployeeUpdateRequest;
import com.sprint.Book_Partner_Application.employee.dto.request.JobCreateRequest;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.JobResponse;
import com.sprint.Book_Partner_Application.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ───────────── JOB APIs ─────────────

    @PostMapping("/jobs")
    public ResponseEntity<ApiResponse<JobResponse>> createJob(@RequestBody JobCreateRequest request) {
        JobResponse response = employeeService.createJob(request);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("Job created successfully", response));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<PageResponse<JobResponse>>> getAllJobs(

            @PageableDefault(size = 10) Pageable pageable) {

        PageResponse<JobResponse> response =
                employeeService.getAllJobs( pageable);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Short jobId) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.getJobById(jobId)));
    }

    @PutMapping("/jobs/{jobId}")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @PathVariable Short jobId,
            @RequestBody JobCreateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success("Job updated successfully",
                        employeeService.updateJob(jobId, request))
        );
    }

    // ───────────── EMPLOYEE APIs ─────────────

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
            @RequestBody EmployeeCreateRequest request) {

        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("Employee created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<EmployeeResponse>>> getAllEmployees(
           
            @PageableDefault(size = 10) Pageable pageable) {

        PageResponse<EmployeeResponse> response =
                employeeService.getAllEmployees( pageable);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{empId}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@PathVariable String empId) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.getEmployeeById(empId)));
    }

    @PutMapping("/{empId}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable String empId,
            @RequestBody EmployeeUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success("Employee updated successfully",
                        employeeService.updateEmployee(empId, request))
        );
    }

    @DeleteMapping("/{empId}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable String empId) {
        employeeService.deleteEmployee(empId);
        return ResponseEntity.ok(ApiResponse.successMessage("Employee deleted successfully"));
    }

    // ───────────── FILTER BY PUBLISHER ─────────────

    @GetMapping("/publisher/{pubId}")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getEmployeesByPublisher(
            @PathVariable String pubId) {

        return ResponseEntity.ok(ApiResponse.success(
                employeeService.getEmployeesByPublisher(pubId)
        ));
    }
}