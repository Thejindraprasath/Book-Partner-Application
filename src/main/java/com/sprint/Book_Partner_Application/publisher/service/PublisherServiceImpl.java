package com.sprint.Book_Partner_Application.publisher.service;

import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.employee.entity.Employee;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import com.sprint.Book_Partner_Application.exception.BusinessValidationException;
import com.sprint.Book_Partner_Application.exception.DuplicateResourceException;
import com.sprint.Book_Partner_Application.exception.ResourceInUseException;
import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherCreateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherUpdateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.response.PublisherResponse;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final EmployeeRepository employeeRepository;
    private final TitleRepository titleRepository;

    @Override
    public PublisherResponse createPublisher(PublisherCreateRequest request) {

        if (publisherRepository.existsById(request.getPubId())) {
            throw new DuplicateResourceException("Publisher", "pubId", request.getPubId());
        }

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
    public PageResponse<PublisherResponse> getAllPublishers(
            String city, String state, String country, Pageable pageable) {

        Page<Publisher> page = publisherRepository.findWithFilters(city, state, country, pageable);

        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PublisherResponse getPublisherById(String pubId) {

        Publisher publisher = publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));

        return mapToResponse(publisher);
    }

    @Override
    public PublisherResponse updatePublisher(String pubId, PublisherUpdateRequest request) {

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

    @Override
    public void deletePublisher(String pubId) {

        Publisher publisher = publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));

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
    public List<EmployeeResponse> getEmployeesByPartner(String pubId) {

        publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));

        return employeeRepository.findByPublisher_PubId(pubId)
                .stream()
                .map(this::mapEmployee)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TitleDTO.Response> getProductsByPartner(String pubId) {

        publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", pubId));

        List<Title> titles = titleRepository
                .findByPublisher_PubId(pubId, Pageable.unpaged())
                .getContent();

        return titles.stream()
                .map(this::mapTitle)
                .toList();
    }

    // ─────────────── MAPPERS ───────────────

    private PublisherResponse mapToResponse(Publisher p) {
        return PublisherResponse.builder()
                .pubId(p.getPubId())
                .pubName(p.getPubName())
                .city(p.getCity())
                .state(p.getState())
                .country(p.getCountry())
                .build();
    }

    private EmployeeResponse mapEmployee(Employee e) {
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