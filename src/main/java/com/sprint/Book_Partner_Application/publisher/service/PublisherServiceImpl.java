package com.sprint.Book_Partner_Application.publisher.service;

import com.sprint.Book_Partner_Application.book.dto.TitleDTO;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.EmployeeDTO;
import com.sprint.Book_Partner_Application.employee.entity.Employee;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import com.sprint.Book_Partner_Application.exception.*;
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

        // Duplicate check
        if (publisherRepository.existsById(request.getPubId())) {
            throw new DuplicateResourceException("Publisher", "pubId", request.getPubId());
        }

        // Business validation
        if (request.getState() != null && request.getState().length() != 2) {
            throw new BusinessValidationException("state", "must be exactly 2 characters");
        }

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
    public PageResponse<PublisherDTO.Response> getAllPublishers(
            String city, String state, String country, Pageable pageable) {

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

        if (request.getState() != null) {
            if (request.getState().length() != 2) {
                throw new BusinessValidationException("state", "must be exactly 2 characters");
            }
            publisher.setState(request.getState());
        }

        if (request.getCountry() != null) publisher.setCountry(request.getCountry());

        return mapToResponse(publisherRepository.save(publisher));
    }

    // ───────────────── DELETE ─────────────────

    @Override
    public void deletePublisher(String pubId) {

        Publisher publisher = publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));

        // 🔴 Check child dependencies
        boolean hasEmployees = employeeRepository.existsByPublisher_PubId(pubId);
        boolean hasTitles = titleRepository.existsByPublisher_PubId(pubId);

        if (hasEmployees || hasTitles) {
            throw new ResourceInUseException(
                    "Publisher",
                    pubId,
                    (hasEmployees ? "employees " : "") + (hasTitles ? "titles" : "")
            );
        }

        publisherRepository.delete(publisher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO.Response> getEmployeesByPartner(String pubId) {

        publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));

        return employeeRepository.findByPublisher_PubId(pubId)
                .stream()
                .map(this::mapEmployee)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TitleDTO.Response> getProductsByPartner(String pubId) {

        publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));

        List<Title> titles = titleRepository.findByPublisher_PubId(pubId, Pageable.unpaged()).getContent();

        return titles.stream()
                .map(this::mapTitle)
                .collect(Collectors.toList());
    }

    // ───────────────── MAPPERS ─────────────────

    private PublisherDTO.Response mapToResponse(Publisher p) {
        return PublisherDTO.Response.builder()
                .pubId(p.getPubId())
                .pubName(p.getPubName())
                .city(p.getCity())
                .state(p.getState())
                .country(p.getCountry())
                .build();
    }

    private EmployeeDTO.Response mapEmployee(Employee e) {
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

    private TitleDTO.Response mapTitle(Title t) {
        return TitleDTO.Response.builder()
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
                .build();
    }
}