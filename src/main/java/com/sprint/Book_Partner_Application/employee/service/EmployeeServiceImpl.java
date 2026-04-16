package com.sprint.Book_Partner_Application.employee.service;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.EmployeeDTO;
import com.sprint.Book_Partner_Application.employee.dto.JobDTO;
import com.sprint.Book_Partner_Application.employee.entity.Employee;
import com.sprint.Book_Partner_Application.employee.entity.Job;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import com.sprint.Book_Partner_Application.employee.repository.JobRepository;
import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final PublisherRepository publisherRepository;

    // ── JOBS ──────────────────────────────────────────────────────────────────

    @Override
    public JobDTO.Response createJob(JobDTO.Request request) {
        Job job = Job.builder()
                .jobDesc(request.getJobDesc())
                .minLvl(request.getMinLvl())
                .maxLvl(request.getMaxLvl())
                .build();
        return mapJobToResponse(jobRepository.save(job));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobDTO.Response> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::mapJobToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobDTO.Response getJobById(Short jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "jobId", jobId));
        return mapJobToResponse(job);
    }

    @Override
    public JobDTO.Response updateJob(Short jobId, JobDTO.Request request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "jobId", jobId));
        job.setJobDesc(request.getJobDesc());
        job.setMinLvl(request.getMinLvl());
        job.setMaxLvl(request.getMaxLvl());
        return mapJobToResponse(jobRepository.save(job));
    }

    // ── EMPLOYEES ─────────────────────────────────────────────────────────────

    @Override
    public EmployeeDTO.Response createEmployee(EmployeeDTO.Request request) {
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job", "jobId", request.getJobId()));
        Publisher publisher = publisherRepository.findById(request.getPubId())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", request.getPubId()));

        Employee employee = Employee.builder()
                .empId(request.getEmpId())
                .fname(request.getFname())
                .minit(request.getMinit())
                .lname(request.getLname())
                .job(job)
                .jobLvl(request.getJobLvl() != null ? request.getJobLvl() : 10)
                .publisher(publisher)
                .hireDate(request.getHireDate() != null ? request.getHireDate() : LocalDateTime.now())
                .build();
        return mapEmpToResponse(employeeRepository.save(employee));
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<EmployeeDTO.Response> getAllEmployees(String pubId, Short jobId, Pageable pageable) {
        Page<Employee> page = employeeRepository.findWithFilters(pubId, jobId, pageable);
        return PageResponse.from(page.map(this::mapEmpToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO.Response getEmployeeById(String empId) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "empId", empId));
        return mapEmpToResponse(emp);
    }

    @Override
    public EmployeeDTO.Response updateEmployee(String empId, EmployeeDTO.UpdateRequest request) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "empId", empId));
        if (request.getFname() != null) emp.setFname(request.getFname());
        if (request.getMinit() != null) emp.setMinit(request.getMinit());
        if (request.getLname() != null) emp.setLname(request.getLname());
        if (request.getJobId() != null) {
            Job job = jobRepository.findById(request.getJobId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job", "jobId", request.getJobId()));
            emp.setJob(job);
        }
        if (request.getJobLvl() != null) emp.setJobLvl(request.getJobLvl());
        if (request.getPubId() != null) {
            Publisher pub = publisherRepository.findById(request.getPubId())
                    .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", request.getPubId()));
            emp.setPublisher(pub);
        }
        if (request.getHireDate() != null) emp.setHireDate(request.getHireDate());
        return mapEmpToResponse(employeeRepository.save(emp));
    }

    @Override
    public void deleteEmployee(String empId) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "empId", empId));
        employeeRepository.delete(emp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO.Response> getEmployeesByPartner(String pubId) {
        publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));
        return employeeRepository.findByPublisher_PubId(pubId).stream()
                .map(this::mapEmpToResponse)
                .collect(Collectors.toList());
    }

    // ── MAPPERS ───────────────────────────────────────────────────────────────

    private JobDTO.Response mapJobToResponse(Job j) {
        return JobDTO.Response.builder()
                .jobId(j.getJobId())
                .jobDesc(j.getJobDesc())
                .minLvl(j.getMinLvl())
                .maxLvl(j.getMaxLvl())
                .build();
    }

    private EmployeeDTO.Response mapEmpToResponse(Employee e) {
        return EmployeeDTO.Response.builder()
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

