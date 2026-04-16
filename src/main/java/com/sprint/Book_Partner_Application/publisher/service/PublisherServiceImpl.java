package com.sprint.Book_Partner_Application.publisher.service;

import com.sprint.Book_Partner_Application.book.dto.TitleDTO;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.EmployeeDTO;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;
import com.sprint.Book_Partner_Application.publisher.dto.PublisherDTO;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final EmployeeRepository employeeRepository;
    private final TitleRepository titleRepository;

    @Override
    public PublisherDTO.Response createPublisher(PublisherDTO.Request request) {
        Publisher publisher = Publisher.builder()
                .pubId(request.getPubId())
                .pubName(request.getPubName())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry() != null ? request.getCountry() : "USA")
                .build();
        return mapToResponse(publisherRepository.save(publisher));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PublisherDTO.Response> getAllPublishers(String city, String state, String country, Pageable pageable) {
        Page<Publisher> page = publisherRepository.findWithFilters(city, state, country, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PublisherDTO.Response getPublisherById(String pubId) {
        Publisher publisher = publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));
        return mapToResponse(publisher);
    }

    @Override
    public PublisherDTO.Response updatePublisher(String pubId, PublisherDTO.UpdateRequest request) {
        Publisher publisher = publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));
        if (request.getPubName() != null) publisher.setPubName(request.getPubName());
        if (request.getCity() != null) publisher.setCity(request.getCity());
        if (request.getState() != null) publisher.setState(request.getState());
        if (request.getCountry() != null) publisher.setCountry(request.getCountry());
        return mapToResponse(publisherRepository.save(publisher));
    }

    @Override
    public void deletePublisher(String pubId) {
        Publisher publisher = publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));
        publisherRepository.delete(publisher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO.Response> getEmployeesByPartner(String pubId) {
        publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));
        return employeeRepository.findByPublisher_PubId(pubId).stream()
                .map(e -> EmployeeDTO.Response.builder()
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
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TitleDTO.Response> getProductsByPartner(String pubId) {
        publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));
        return titleRepository.findByPublisher_PubId(pubId, Pageable.unpaged()).stream()
                .map(t -> TitleDTO.Response.builder()
                        .titleId(t.getTitleId())
                        .title(t.getTitle())
                        .type(t.getType())
                        .pubId(t.getPublisher() != null ? t.getPublisher().getPubId() : null)
                        .pubName(t.getPublisher() != null ? t.getPublisher().getPubName() : null)
                        .price(t.getPrice())
                        .advance(t.getAdvance())
                        .royalty(t.getRoyalty())
                        .ytdSales(t.getYtdSales())
                        .notes(t.getNotes())
                        .pubdate(t.getPubdate())
                        .build())
                .collect(Collectors.toList());
    }

    private PublisherDTO.Response mapToResponse(Publisher p) {
        return PublisherDTO.Response.builder()
                .pubId(p.getPubId())
                .pubName(p.getPubName())
                .city(p.getCity())
                .state(p.getState())
                .country(p.getCountry())
                .build();
    }}
