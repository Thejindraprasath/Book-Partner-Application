package com.sprint.Book_Partner_Application.employee.service;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.request.EmployeeCreateRequest;
import com.sprint.Book_Partner_Application.employee.dto.request.EmployeeUpdateRequest;
import com.sprint.Book_Partner_Application.employee.dto.request.JobCreateRequest;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.JobResponse;
import com.sprint.Book_Partner_Application.employee.entity.Employee;
import com.sprint.Book_Partner_Application.employee.entity.Job;
import com.sprint.Book_Partner_Application.employee.exception.*;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import com.sprint.Book_Partner_Application.employee.repository.JobRepository;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.exception.PublisherNotFoundException;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import com.sprint.Book_Partner_Application.exception.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    PublisherRepository publisherRepository;

    // ════════════════════════════════════════════════════════
    // JOBS
    // ════════════════════════════════════════════════════════

    @Override
    public JobResponse createJob(JobCreateRequest request) {
        log.debug("Creating job: {}", request.getJobDesc());

        if (request.getMinLvl() >= request.getMaxLvl()
                || request.getMinLvl() < 10
                || request.getMaxLvl() > 250) {
            throw new InvalidJobLevelRangeException(
                    request.getMinLvl(),
                    request.getMaxLvl()
            );
        }

        Job job = Job.builder()
                .jobDesc(request.getJobDesc())
                .minLvl(request.getMinLvl())
                .maxLvl(request.getMaxLvl())
                .build();

        Job saved = jobRepository.save(job);

        log.info("Job created: {}", saved.getJobId());
        return mapJobToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapJobToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getJobById(Short jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        return mapJobToResponse(job);
    }

    @Override
    public JobResponse updateJob(Short jobId, JobCreateRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        if (request.getMinLvl() >= request.getMaxLvl()
                || request.getMinLvl() < 10
                || request.getMaxLvl() > 250) {
            throw new InvalidJobLevelRangeException(
                    request.getMinLvl(),
                    request.getMaxLvl()
            );
        }

        // Check existing employees before narrowing range
        employeeRepository.findByJob_JobId(jobId).forEach(emp -> {
            if (emp.getJobLvl() < request.getMinLvl()
                    || emp.getJobLvl() > request.getMaxLvl()) {

                throw new JobLevelUpdateBreaksEmployeesException(
                        emp.getEmpId(),
                        emp.getJobLvl(),
                        request.getMinLvl(),
                        request.getMaxLvl()
                );
            }
        });

        job.setJobDesc(request.getJobDesc());
        job.setMinLvl(request.getMinLvl());
        job.setMaxLvl(request.getMaxLvl());

        Job updated = jobRepository.save(job);

        log.info("Job updated: {}", jobId);
        return mapJobToResponse(updated);
    }

    // ════════════════════════════════════════════════════════
    // EMPLOYEES
    // ════════════════════════════════════════════════════════

    @Override
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        log.debug("Creating employee: {}", request.getEmpId());

        if (employeeRepository.existsById(request.getEmpId())) {
            throw new EmployeeAlreadyExistsException(request.getEmpId());
        }

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new JobNotFoundException(request.getJobId()));

        Publisher publisher = publisherRepository.findById(request.getPubId())
                .orElseThrow(() -> new PublisherNotFoundException(request.getPubId()));

        int jobLvl = request.getJobLvl() != null ? request.getJobLvl() : 10;

        if (jobLvl < job.getMinLvl() || jobLvl > job.getMaxLvl()) {
            throw new EmployeeJobLevelMismatchException(
                    jobLvl,
                    job.getJobDesc(),
                    job.getMinLvl(),
                    job.getMaxLvl()
            );
        }

        Employee employee = Employee.builder()
                .empId(request.getEmpId())
                .fname(request.getFname())
                .minit(request.getMinit())
                .lname(request.getLname())
                .job(job)
                .jobLvl(jobLvl)
                .publisher(publisher)
                .hireDate(request.getHireDate() != null
                        ? request.getHireDate()
                        : LocalDateTime.now())
                .build();

        Employee saved = employeeRepository.save(employee);

        log.info("Employee created: {}", saved.getEmpId());
        return mapEmpToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<EmployeeResponse> getAllEmployees(
            String pubId,
            Short jobId,
            Pageable pageable) {

        if (pubId != null && !publisherRepository.existsById(pubId)) {
            throw new PublisherNotFoundException(pubId);
        }

        if (jobId != null && !jobRepository.existsById(jobId)) {
            throw new JobNotFoundException(jobId);
        }

        return PageResponse.from(
                employeeRepository
                        .findWithFilters(pubId, jobId, pageable)
                        .map(this::mapEmpToResponse)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(String empId) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new EmployeeNotFoundException(empId));

        return mapEmpToResponse(emp);
    }

    @Override
    public EmployeeResponse updateEmployee(String empId, EmployeeUpdateRequest request) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new EmployeeNotFoundException(empId));

        Job jobForValidation = emp.getJob();

        if (request.getJobId() != null) {
            jobForValidation = jobRepository.findById(request.getJobId())
                    .orElseThrow(() -> new JobNotFoundException(request.getJobId()));
            emp.setJob(jobForValidation);
        }

        if (request.getJobLvl() != null && jobForValidation != null) {
            if (request.getJobLvl() < jobForValidation.getMinLvl()
                    || request.getJobLvl() > jobForValidation.getMaxLvl()) {

                throw new EmployeeJobLevelMismatchException(
                        request.getJobLvl(),
                        jobForValidation.getJobDesc(),
                        jobForValidation.getMinLvl(),
                        jobForValidation.getMaxLvl()
                );
            }
        }

        if (request.getPubId() != null) {
            Publisher pub = publisherRepository.findById(request.getPubId())
                    .orElseThrow(() -> new PublisherNotFoundException(request.getPubId()));
            emp.setPublisher(pub);
        }

        if (request.getFname() != null) emp.setFname(request.getFname());
        if (request.getMinit() != null) emp.setMinit(request.getMinit());
        if (request.getLname() != null) emp.setLname(request.getLname());
        if (request.getJobLvl() != null) emp.setJobLvl(request.getJobLvl());
        if (request.getHireDate() != null) emp.setHireDate(request.getHireDate());

        Employee updated = employeeRepository.save(emp);

        log.info("Employee updated: {}", empId);
        return mapEmpToResponse(updated);
    }

    @Override
    public void deleteEmployee(String empId) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new EmployeeNotFoundException(empId));

        employeeRepository.delete(emp);

        log.info("Employee deleted: {}", empId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByPartner(String pubId) {
        publisherRepository.findById(pubId)
                .orElseThrow(() -> new PublisherNotFoundException(pubId));

        return employeeRepository.findByPublisher_PubId(pubId)
                .stream()
                .map(this::mapEmpToResponse)
                .collect(Collectors.toList());
    }

    // ════════════════════════════════════════════════════════
    // MAPPERS
    // ════════════════════════════════════════════════════════

    private JobResponse mapJobToResponse(Job j) {
        return JobResponse.builder()
                .jobId(j.getJobId())
                .jobDesc(j.getJobDesc())
                .minLvl(j.getMinLvl())
                .maxLvl(j.getMaxLvl())
                .build();
    }

    private EmployeeResponse mapEmpToResponse(Employee e) {
        return EmployeeResponse.builder()
                .empId(e.getEmpId())
                .fname(e.getFname())
                .minit(e.getMinit())
                .lname(e.getLname())
                .jobId(e.getJob() != null ? e.getJob().getJobId() : null)
                .jobDesc(e.getJob() != null ? e.getJob().getJobDesc() : null)
                .jobLvl(e.getJobLvl())
                .pubId(e.getPublisher() != null ? e.getPublisher().getPubId() : null)
                .pubName(e.getPublisher() != null ? e.getPublisher().getPubName() : null)
                .hireDate(e.getHireDate())
                .build();
    }
}