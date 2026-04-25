package com.sprint.Book_Partner_Application.publisher.service;

import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.employee.entity.Employee;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherCreateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherUpdateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.response.PublisherResponse;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.exception.*;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {

    private static final Set<String> STANDARD_PUB_IDS =
            new HashSet<>(Arrays.asList("1389", "0736", "0877", "1622", "1756"));

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TitleRepository titleRepository;

    // CREATE
    @Override
    public PublisherResponse createPublisher(PublisherCreateRequest request) {

        if (publisherRepository.existsById(request.getPubId())) {
            throw new PublisherAlreadyExistsException(request.getPubId());
        }

        boolean isStandard = STANDARD_PUB_IDS.contains(request.getPubId());
        boolean isNinetyNineX = request.getPubId().matches("^99[0-9]{2}$");

        if (!isStandard && !isNinetyNineX) {
            throw new InvalidPublisherIdException(request.getPubId());
        }

        Publisher publisher = new Publisher();
        publisher.setPubId(request.getPubId());
        publisher.setPubName(request.getPubName());
        publisher.setCity(request.getCity());
        publisher.setState(request.getState());
        publisher.setCountry(request.getCountry() != null ? request.getCountry() : "USA");

        Publisher saved = publisherRepository.save(publisher);

        return mapToResponse(saved);
    }

    // READ ALL
    @Override
    @Transactional(readOnly = true)
    public PageResponse<PublisherResponse> getAllPublishers(Pageable pageable) {

        Page<Publisher> page = publisherRepository.findWithFilters(pageable);

        List<PublisherResponse> responseList = new ArrayList<>();

        for (Publisher p : page.getContent()) {
            responseList.add(mapToResponse(p));
        }

        return PageResponse.from(page, responseList);
    }

    // READ ONE
    @Override
    @Transactional(readOnly = true)
    public PublisherResponse getPublisherById(String pubId) {

        Publisher publisher = publisherRepository.findById(pubId)
                .orElseThrow(() -> new PublisherNotFoundException(pubId));

        return mapToResponse(publisher);
    }

    // UPDATE
    @Override
    public PublisherResponse updatePublisher(String pubId, PublisherUpdateRequest request) {

        Publisher publisher = publisherRepository.findById(pubId)
                .orElseThrow(() -> new PublisherNotFoundException(pubId));

        if (request.getPubName() != null) {
            publisher.setPubName(request.getPubName());
        }
        if (request.getCity() != null) {
            publisher.setCity(request.getCity());
        }
        if (request.getState() != null) {
            publisher.setState(request.getState());
        }
        if (request.getCountry() != null) {
            publisher.setCountry(request.getCountry());
        }

        Publisher updated = publisherRepository.save(publisher);

        return mapToResponse(updated);
    }

    // DELETE
    @Override
    public void deletePublisher(String pubId) {

        Publisher publisher = publisherRepository.findById(pubId)
                .orElseThrow(() -> new PublisherNotFoundException(pubId));

        long empCount = employeeRepository.findByPublisher_PubId(pubId).size();
        if (empCount > 0) {
            throw new PublisherHasActiveEmployeesException(pubId, empCount);
        }

        long titleCount = titleRepository
                .findByPublisher_PubId(pubId, Pageable.unpaged())
                .getTotalElements();

        if (titleCount > 0) {
            throw new PublisherHasActiveTitlesException(pubId, titleCount);
        }

        publisherRepository.delete(publisher);
    }

    // EMPLOYEES
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByPublisher(String pubId) {

        publisherRepository.findById(pubId)
                .orElseThrow(() -> new PublisherNotFoundException(pubId));

        List<Employee> employees =
                employeeRepository.findByPublisher_PubId(pubId);

        List<EmployeeResponse> responseList = new ArrayList<>();

        for (Employee e : employees) {

            EmployeeResponse res = new EmployeeResponse();

            res.setEmpId(e.getEmpId());
            res.setFname(e.getFname());
            res.setMinit(e.getMinit());
            res.setLname(e.getLname());

            if (e.getJob() != null) {
                res.setJobId(e.getJob().getJobId());
                res.setJobDesc(e.getJob().getJobDesc());
            }

            res.setJobLvl(e.getJobLvl());

            if (e.getPublisher() != null) {
                res.setPubId(e.getPublisher().getPubId());
                res.setPubName(e.getPublisher().getPubName());
            }

            res.setHireDate(e.getHireDate());

            responseList.add(res);
        }

        return responseList;
    }

    // TITLES
    @Override
    @Transactional(readOnly = true)
    public List<TitleResponse> getTitlesByPublisher(String pubId) {

        publisherRepository.findById(pubId)
                .orElseThrow(() -> new PublisherNotFoundException(pubId));

        List<Title> titles =
                titleRepository
                        .findByPublisher_PubId(pubId, Pageable.unpaged())
                        .getContent();

        List<TitleResponse> responseList = new ArrayList<>();

        for (Title t : titles) {

            TitleResponse res = new TitleResponse();

            res.setTitleId(t.getTitleId());
            res.setTitle(t.getTitle());
            res.setType(t.getType());

            if (t.getPublisher() != null) {
                res.setPubId(t.getPublisher().getPubId());
                res.setPubName(t.getPublisher().getPubName());
            }

            res.setPrice(t.getPrice());
            res.setAdvance(t.getAdvance());
            res.setRoyalty(t.getRoyalty());
            res.setYtdSales(t.getYtdSales());
            res.setNotes(t.getNotes());
            res.setPubdate(t.getPubdate());

            responseList.add(res);
        }

        return responseList;
    }

    // MAPPER
    private PublisherResponse mapToResponse(Publisher publisher) {

        PublisherResponse response = new PublisherResponse();
        response.setPubId(publisher.getPubId());
        response.setPubName(publisher.getPubName());
        response.setCity(publisher.getCity());
        response.setState(publisher.getState());
        response.setCountry(publisher.getCountry());

        return response;
    }
}