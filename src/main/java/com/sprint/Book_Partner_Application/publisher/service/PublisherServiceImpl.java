package com.sprint.Book_Partner_Application.publisher.service;

import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherCreateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherUpdateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.response.PublisherResponse;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.exception.*;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {

    private static final Set<String> STANDARD_PUB_IDS =
            Set.of("1389", "0736", "0877", "1622", "1756");
    @Autowired
     private PublisherRepository publisherRepository;
    @Autowired
     private EmployeeRepository employeeRepository;
    @Autowired
     private TitleRepository titleRepository;

    // ─── CREATE ─────────────────────────────────────────────

    @Override
    public PublisherResponse createPublisher(PublisherCreateRequest request) {
        log.debug("Creating publisher: {}", request.getPubId());

        if (publisherRepository.existsById(request.getPubId())) {
            throw new PublisherAlreadyExistsException(request.getPubId());
        }

        boolean isStandard = STANDARD_PUB_IDS.contains(request.getPubId());
        boolean isNinetyNineX = request.getPubId().matches("^99[0-9]{2}$");

        if (!isStandard && !isNinetyNineX) {
            throw new InvalidPublisherIdException(request.getPubId());
        }

        Publisher publisher = Publisher.builder()
                .pubId(request.getPubId())
                .pubName(request.getPubName())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry() != null ? request.getCountry() : "USA")
                .build();

        Publisher saved = publisherRepository.save(publisher);

        log.info("Publisher created: {}", saved.getPubId());
        return mapToResponse(saved);
    }

    // ─── READ ALL ─────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PublisherResponse> getAllPublishers(
            String city,
            String state,
            String country,
            Pageable pageable) {

        return PageResponse.from(
                publisherRepository
                        .findWithFilters(city, state, country, pageable)
                        .map(this::mapToResponse)
        );
    }

    // ─── READ ONE ─────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PublisherResponse getPublisherById(String pubId) {
        Publisher publisher = publisherRepository.findById(pubId)
                .orElseThrow(() -> new PublisherNotFoundException(pubId));

        return mapToResponse(publisher);
    }

    // ─── UPDATE ─────────────────────────────────────────────

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

        log.info("Publisher updated: {}", pubId);
        return mapToResponse(updated);
    }

    // ─── DELETE ─────────────────────────────────────────────

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
        log.info("Publisher deleted: {}", pubId);
    }

    // ─── EMPLOYEES BY PARTNER ─────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByPartner(String pubId) {
        publisherRepository.findById(pubId)
                .orElseThrow(() -> new PublisherNotFoundException(pubId));

        return employeeRepository.findByPublisher_PubId(pubId)
                .stream()
                .map(e -> EmployeeResponse.builder()
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

    // ─── PRODUCTS BY PARTNER ─────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<TitleResponse> getProductsByPartner(String pubId) {
        publisherRepository.findById(pubId)
                .orElseThrow(() -> new PublisherNotFoundException(pubId));

        return titleRepository
                .findByPublisher_PubId(pubId, Pageable.unpaged())
                .getContent()
                .stream()
                .map(t -> TitleResponse.builder()
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

    // ─── MAPPER ─────────────────────────────────────────────

    private PublisherResponse mapToResponse(Publisher publisher) {
        return PublisherResponse.builder()
                .pubId(publisher.getPubId())
                .pubName(publisher.getPubName())
                .city(publisher.getCity())
                .state(publisher.getState())
                .country(publisher.getCountry())
                .build();
    }
}