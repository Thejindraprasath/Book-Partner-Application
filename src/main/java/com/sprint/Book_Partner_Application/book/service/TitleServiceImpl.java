package com.sprint.Book_Partner_Application.book.service;

import com.sprint.Book_Partner_Application.book.dto.request.TitleCreateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.TitleUpdateRequest;
import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.exception.*;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TitleServiceImpl implements TitleService {

    private final TitleRepository titleRepository;
    private final PublisherRepository publisherRepository;

    // ✅ CREATE
    @Override
    public TitleResponse createTitle(TitleCreateRequest request) {

        if (titleRepository.existsById(request.getTitleId())) {
            throw new DuplicateResourceException("Title", "titleId", request.getTitleId());
        }

        Publisher publisher = null;
        if (request.getPubId() != null) {
            publisher = publisherRepository.findById(request.getPubId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Publisher", "pubId", request.getPubId()));
        }

        Title title = Title.builder()
                .titleId(request.getTitleId())
                .title(request.getTitle())
                .type(request.getType())
                .publisher(publisher)
                .price(request.getPrice())
                .advance(request.getAdvance())
                .royalty(request.getRoyalty())
                .ytdSales(request.getYtdSales())
                .notes(request.getNotes())
                .pubdate(request.getPubdate())
                .build();

        return mapToResponse(titleRepository.save(title));
    }

    // ✅ GET ALL
    @Override
    @Transactional(readOnly = true)
    public PageResponse<TitleResponse> getAllTitles(
            String type, String pubId,
            Double minPrice, Double maxPrice,
            Pageable pageable) {

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new InvalidOperationException("minPrice cannot be greater than maxPrice");
        }

        Page<Title> page = titleRepository.findWithFilters(type, pubId, minPrice, maxPrice, pageable);

        return PageResponse.from(page.map(this::mapToResponse));
    }

    // ✅ GET BY ID
    @Override
    public TitleResponse getTitleById(String titleId) {
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Title", "titleId", titleId));

        return mapToResponse(title);
    }

    // ✅ UPDATE
    @Override
    public TitleResponse updateTitle(String titleId, TitleUpdateRequest request) {

        Title title = titleRepository.findById(titleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Title", "titleId", titleId));

        if (request.getTitle() != null) title.setTitle(request.getTitle());
        if (request.getType() != null) title.setType(request.getType());

        if (request.getPubId() != null) {
            Publisher publisher = publisherRepository.findById(request.getPubId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Publisher", "pubId", request.getPubId()));
            title.setPublisher(publisher);
        }

        if (request.getPrice() != null) title.setPrice(request.getPrice());
        if (request.getAdvance() != null) title.setAdvance(request.getAdvance());
        if (request.getRoyalty() != null) title.setRoyalty(request.getRoyalty());
        if (request.getYtdSales() != null) title.setYtdSales(request.getYtdSales());
        if (request.getNotes() != null) title.setNotes(request.getNotes());
        if (request.getPubdate() != null) title.setPubdate(request.getPubdate());

        return mapToResponse(titleRepository.save(title));
    }

    // ✅ DELETE
    @Override
    public void deleteTitle(String titleId) {
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Title", "titleId", titleId));

        titleRepository.delete(title);
    }

    // ✅ MAPPER
    private TitleResponse mapToResponse(Title t) {

        Publisher p = t.getPublisher();

        return TitleResponse.builder()
                .titleId(t.getTitleId())
                .title(t.getTitle())
                .type(t.getType())
                .pubId(p != null ? p.getPubId() : null)
                .pubName(p != null ? p.getPubName() : null)
                .price(t.getPrice())
                .advance(t.getAdvance())
                .royalty(t.getRoyalty())
                .ytdSales(t.getYtdSales())
                .notes(t.getNotes())
                .pubdate(t.getPubdate())
                .build();
    }
}