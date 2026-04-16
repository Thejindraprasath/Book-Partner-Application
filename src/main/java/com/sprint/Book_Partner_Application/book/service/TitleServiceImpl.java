package com.sprint.Book_Partner_Application.book.service;

import com.sprint.Book_Partner_Application.author.dto.AuthorDTO;
import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.author.repository.TitleAuthorRepository;
import com.sprint.Book_Partner_Application.book.dto.RoySchedDTO;
import com.sprint.Book_Partner_Application.book.dto.TitleDTO;
import com.sprint.Book_Partner_Application.book.entity.RoySched;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.RoySchedRepository;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;
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

public class TitleServiceImpl implements TitleService {
    private final TitleRepository titleRepository;
    private final PublisherRepository publisherRepository;
    private final TitleAuthorRepository titleAuthorRepository;
    private final RoySchedRepository roySchedRepository;

    @Override
    public TitleDTO.Response createTitle(TitleDTO.Request request) {
        Publisher publisher = null;
        if (request.getPubId() != null) {
            publisher = publisherRepository.findById(request.getPubId())
                    .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", request.getPubId()));
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

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TitleDTO.Response> getAllTitles(String type, String pubId, Double minPrice, Double maxPrice, Pageable pageable) {
        Page<Title> page = titleRepository.findWithFilters(type, pubId, minPrice, maxPrice, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public TitleDTO.Response getTitleById(String titleId) {
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new ResourceNotFoundException("Title", "titleId", titleId));
        return mapToResponse(title);
    }

    @Override
    public TitleDTO.Response updateTitle(String titleId, TitleDTO.UpdateRequest request) {
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new ResourceNotFoundException("Title", "titleId", titleId));

        if (request.getTitle() != null) title.setTitle(request.getTitle());
        if (request.getType() != null) title.setType(request.getType());
        if (request.getPubId() != null) {
            Publisher publisher = publisherRepository.findById(request.getPubId())
                    .orElseThrow(() -> new ResourceNotFoundException("Publisher", "pubId", request.getPubId()));
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

    @Override
    public void deleteTitle(String titleId) {
        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new ResourceNotFoundException("Title", "titleId", titleId));
        titleRepository.delete(title);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO.Response> getAuthorsByTitle(String titleId) {
        titleRepository.findById(titleId)
                .orElseThrow(() -> new ResourceNotFoundException("Title", "titleId", titleId));
        return titleAuthorRepository.findByTitleId(titleId).stream()
                .filter(ta -> ta.getAuthor() != null)
                .map(TitleAuthor::getAuthor)
                .map(a -> AuthorDTO.Response.builder()
                        .auId(a.getAuId())
                        .auLname(a.getAuLname())
                        .auFname(a.getAuFname())
                        .phone(a.getPhone())
                        .address(a.getAddress())
                        .city(a.getCity())
                        .state(a.getState())
                        .zip(a.getZip())
                        .contract(a.getContract())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoySchedDTO.Response> getRoySchedsByTitle(String titleId) {
        titleRepository.findById(titleId)
                .orElseThrow(() -> new ResourceNotFoundException("Title", "titleId", titleId));
        return roySchedRepository.findByTitle_TitleId(titleId).stream()
                .map(this::mapRoySchedToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoySchedDTO.Response createRoySched(RoySchedDTO.Request request) {
        Title title = titleRepository.findById(request.getTitleId())
                .orElseThrow(() -> new ResourceNotFoundException("Title", "titleId", request.getTitleId()));
        RoySched rs = RoySched.builder()
                .title(title)
                .lorange(request.getLorange())
                .hirange(request.getHirange())
                .royalty(request.getRoyalty())
                .build();
        return mapRoySchedToResponse(roySchedRepository.save(rs));
    }

    @Override
    public RoySchedDTO.Response updateRoySched(Long roySchedId, RoySchedDTO.Request request) {
        RoySched rs = roySchedRepository.findById(roySchedId)
                .orElseThrow(() -> new ResourceNotFoundException("RoySched", "id", roySchedId));
        if (request.getLorange() != null) rs.setLorange(request.getLorange());
        if (request.getHirange() != null) rs.setHirange(request.getHirange());
        if (request.getRoyalty() != null) rs.setRoyalty(request.getRoyalty());
        return mapRoySchedToResponse(roySchedRepository.save(rs));
    }

    @Override
    public void deleteRoySched(Long roySchedId) {
        RoySched rs = roySchedRepository.findById(roySchedId)
                .orElseThrow(() -> new ResourceNotFoundException("RoySched", "id", roySchedId));
        roySchedRepository.delete(rs);
    }

    private TitleDTO.Response mapToResponse(Title t) {
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

    private RoySchedDTO.Response mapRoySchedToResponse(RoySched rs) {
        return RoySchedDTO.Response.builder()
                .roySchedId(rs.getRoySchedId())
                .titleId(rs.getTitle() != null ? rs.getTitle().getTitleId() : null)
                .titleName(rs.getTitle() != null ? rs.getTitle().getTitle() : null)
                .lorange(rs.getLorange())
                .hirange(rs.getHirange())
                .royalty(rs.getRoyalty())
                .build();
    }
}
