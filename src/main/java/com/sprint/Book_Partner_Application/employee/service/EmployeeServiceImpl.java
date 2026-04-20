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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PublisherRepository publisherRepository;

    // ================= JOB =================

    @Override
    public JobResponse createJob(JobCreateRequest request) {

        if (request.getMinLvl() >= request.getMaxLvl()
                || request.getMinLvl() < 10
                || request.getMaxLvl() > 250) {
            throw new InvalidJobLevelRangeException(
                    request.getMinLvl(),
                    request.getMaxLvl()
            );
        }

        Job job = new Job();
        job.setJobDesc(request.getJobDesc());
        job.setMinLvl(request.getMinLvl());
        job.setMaxLvl(request.getMaxLvl());

        Job saved = jobRepository.save(job);

        return mapJobToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs() {

        List<Job> jobs = jobRepository.findAll();
        List<JobResponse> responseList = new ArrayList<>();

        for (Job j : jobs) {
            JobResponse res = mapJobToResponse(j);
            responseList.add(res);
        }

        return responseList;
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

        return mapJobToResponse(updated);
    }

    // ================= EMPLOYEE =================

    @Override
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {

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

        Employee employee = new Employee();
        employee.setEmpId(request.getEmpId());
        employee.setFname(request.getFname());
        employee.setMinit(request.getMinit());
        employee.setLname(request.getLname());
        employee.setJob(job);
        employee.setJobLvl(jobLvl);
        employee.setPublisher(publisher);
        employee.setHireDate(
                request.getHireDate() != null
                        ? request.getHireDate()
                        : LocalDateTime.now()
        );

        Employee saved = employeeRepository.save(employee);

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

        if (request.getJobLvl() != null) {
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

        return mapEmpToResponse(updated);
    }

    @Override
    public void deleteEmployee(String empId) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new EmployeeNotFoundException(empId));

        employeeRepository.delete(emp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByPublisher(String pubId) {

        publisherRepository.findById(pubId)
                .orElseThrow(() -> new PublisherNotFoundException(pubId));

        List<Employee> employees =
                employeeRepository.findByPublisher_PubId(pubId);

        List<EmployeeResponse> responseList = new ArrayList<>();

        for (Employee e : employees) {
            EmployeeResponse res = mapEmpToResponse(e);
            responseList.add(res);
        }

        return responseList;
    }

    // ================= MAPPERS =================

    private JobResponse mapJobToResponse(Job j) {
        JobResponse res = new JobResponse();
        res.setJobId(j.getJobId());
        res.setJobDesc(j.getJobDesc());
        res.setMinLvl(j.getMinLvl());
        res.setMaxLvl(j.getMaxLvl());
        return res;
    }

    private EmployeeResponse mapEmpToResponse(Employee e) {
        EmployeeResponse res = new EmployeeResponse();
        res.setEmpId(e.getEmpId());
        res.setFname(e.getFname());
        res.setMinit(e.getMinit());
        res.setLname(e.getLname());
        res.setJobId(e.getJob() != null ? e.getJob().getJobId() : null);
        res.setJobDesc(e.getJob() != null ? e.getJob().getJobDesc() : null);
        res.setJobLvl(e.getJobLvl());
        res.setPubId(e.getPublisher() != null ? e.getPublisher().getPubId() : null);
        res.setPubName(e.getPublisher() != null ? e.getPublisher().getPubName() : null);
        res.setHireDate(e.getHireDate());
        return res;
    }
}