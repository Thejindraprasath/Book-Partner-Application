package com.sprint.Book_Partner_Application.book.service;

import com.sprint.Book_Partner_Application.author.dto.response.AuthorResponse;
import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.author.repository.TitleAuthorRepository;
import com.sprint.Book_Partner_Application.book.dto.request.*;
import com.sprint.Book_Partner_Application.book.dto.response.*;
import com.sprint.Book_Partner_Application.book.entity.RoySched;
import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.exception.*;
import com.sprint.Book_Partner_Application.book.repository.RoySchedRepository;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TitleServiceImpl implements TitleService {

    private final TitleRepository titleRepository;
    private final PublisherRepository publisherRepository;
    private final TitleAuthorRepository titleAuthorRepository;
    private final RoySchedRepository roySchedRepository;
    private final SaleRepository saleRepository;

    // ─────────────── CREATE ───────────────

    @Override
    public TitleResponse createTitle(TitleCreateRequest request) {

        if (titleRepository.existsById(request.getTitleId()))
            throw new TitleAlreadyExistsException(request.getTitleId());

        if (request.getType() != null &&
                !InvalidTitleTypeException.VALID_TYPES.contains(request.getType()))
            throw new InvalidTitleTypeException(request.getType());

        if (request.getPrice() != null && request.getPrice() <= 0)
            throw new InvalidPriceException(request.getPrice());

        if (request.getRoyalty() != null &&
                (request.getRoyalty() < 0 || request.getRoyalty() > 100))
            throw new InvalidRoyaltyException(request.getRoyalty());

        Publisher publisher = null;
        if (request.getPubId() != null)
            publisher = publisherRepository.findById(request.getPubId())
                    .orElseThrow(() -> new PublisherNotFoundException(request.getPubId()));

        Title saved = titleRepository.save(Title.builder()
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
                .build());

        return mapToResponse(saved);
    }

    // ─────────────── READ ALL ───────────────

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TitleResponse> getAllTitles(
            String type, String pubId, Double minPrice, Double maxPrice, Pageable pageable) {

        if (type != null &&
                !InvalidTitleTypeException.VALID_TYPES.contains(type))
            throw new InvalidTitleTypeException(type);

        if (minPrice != null && maxPrice != null && minPrice > maxPrice)
            throw new RuntimeException("minPrice cannot be greater than maxPrice"); // FIXED

        if (pubId != null && !publisherRepository.existsById(pubId))
            throw new PublisherNotFoundException(pubId);

        return PageResponse.from(
                titleRepository.findWithFilters(type, pubId, minPrice, maxPrice, pageable)
                        .map(this::mapToResponse)
        );
    }

    // ─────────────── READ ONE ───────────────

    @Override
    @Transactional(readOnly = true)
    public TitleResponse getTitleById(String titleId) {
        return mapToResponse(
                titleRepository.findById(titleId)
                        .orElseThrow(() -> new TitleNotFoundException(titleId))
        );
    }

    // ─────────────── UPDATE ───────────────

    @Override
    public TitleResponse updateTitle(String titleId, TitleUpdateRequest request) {

        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));

        if (request.getType() != null &&
                !InvalidTitleTypeException.VALID_TYPES.contains(request.getType()))
            throw new InvalidTitleTypeException(request.getType());

        if (request.getPrice() != null && request.getPrice() <= 0)
            throw new InvalidPriceException(request.getPrice());

        if (request.getRoyalty() != null &&
                (request.getRoyalty() < 0 || request.getRoyalty() > 100))
            throw new InvalidRoyaltyException(request.getRoyalty());

        if (request.getPubId() != null) {
            Publisher pub = publisherRepository.findById(request.getPubId())
                    .orElseThrow(() -> new PublisherNotFoundException(request.getPubId()));
            title.setPublisher(pub);
        }

        if (request.getTitle() != null) title.setTitle(request.getTitle());
        if (request.getType() != null) title.setType(request.getType());
        if (request.getPrice() != null) title.setPrice(request.getPrice());
        if (request.getAdvance() != null) title.setAdvance(request.getAdvance());
        if (request.getRoyalty() != null) title.setRoyalty(request.getRoyalty());
        if (request.getYtdSales() != null) title.setYtdSales(request.getYtdSales());
        if (request.getNotes() != null) title.setNotes(request.getNotes());
        if (request.getPubdate() != null) title.setPubdate(request.getPubdate());

        return mapToResponse(titleRepository.save(title));
    }

    // ─────────────── DELETE ───────────────

    @Override
    public void deleteTitle(String titleId) {

        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));

        List<?> sales = saleRepository.findByTitleId(titleId);
        if (!sales.isEmpty())
            throw new TitleHasActiveSalesException(titleId, sales.size());

        List<TitleAuthor> links = titleAuthorRepository.findByTitleId(titleId);
        if (!links.isEmpty())
            throw new TitleHasActiveAuthorsException(titleId, links.size());

        titleRepository.delete(title);
    }

    // ─────────────── AUTHORS ───────────────

    @Override
    public List<AuthorResponse> getAuthorsByTitle(String titleId) {

        titleRepository.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));

        return titleAuthorRepository.findByTitleId(titleId).stream()
                .filter(ta -> ta.getAuthor() != null)
                .map(ta -> {
                    var a = ta.getAuthor();
                    return AuthorResponse.builder()
                            .auId(a.getAuId())
                            .auLname(a.getAuLname())
                            .auFname(a.getAuFname())
                            .phone(a.getPhone())
                            .address(a.getAddress())
                            .city(a.getCity())
                            .state(a.getState())
                            .zip(a.getZip())
                            .contract(a.getContract())
                            .build();
                }).collect(Collectors.toList());
    }

    // ─────────────── ROYSCHED ───────────────

    @Override
    public RoySchedResponse createRoySched(RoySchedCreateRequest request) {

        Title title = titleRepository.findById(request.getTitleId())
                .orElseThrow(() -> new TitleNotFoundException(request.getTitleId()));

        validateRange(request.getLorange(), request.getHirange(), request.getTitleId(), null);

        if (request.getRoyalty() != null &&
                (request.getRoyalty() < 0 || request.getRoyalty() > 100))
            throw new InvalidRoyaltyException(request.getRoyalty());

        RoySched saved = roySchedRepository.save(RoySched.builder()
                .title(title)
                .lorange(request.getLorange())
                .hirange(request.getHirange())
                .royalty(request.getRoyalty())
                .build());

        return mapRoySchedToResponse(saved);
    }

    @Override
    public RoySchedResponse updateRoySched(Long roySchedId, RoySchedUpdateRequest request) {

        RoySched rs = roySchedRepository.findById(roySchedId)
                .orElseThrow(() -> new RoySchedNotFoundException(roySchedId));

        int newLo = request.getLorange() != null ? request.getLorange() : rs.getLorange();
        int newHi = request.getHirange() != null ? request.getHirange() : rs.getHirange();

        validateRange(newLo, newHi, rs.getTitle().getTitleId(), roySchedId);

        if (request.getRoyalty() != null &&
                (request.getRoyalty() < 0 || request.getRoyalty() > 100))
            throw new InvalidRoyaltyException(request.getRoyalty());

        if (request.getLorange() != null) rs.setLorange(request.getLorange());
        if (request.getHirange() != null) rs.setHirange(request.getHirange());
        if (request.getRoyalty() != null) rs.setRoyalty(request.getRoyalty());

        return mapRoySchedToResponse(roySchedRepository.save(rs));
    }

    // ─────────────── VALIDATION HELPER ───────────────

    private void validateRange(Integer lo, Integer hi, String titleId, Long currentId) {

        if (lo != null && hi != null) {
            if (lo >= hi)
                throw new InvalidRoySchedRangeException(lo, hi);

            boolean overlaps = roySchedRepository.findByTitle_TitleId(titleId)
                    .stream()
                    .filter(e -> currentId == null || !e.getRoySchedId().equals(currentId))
                    .anyMatch(e -> lo <= e.getHirange() && hi >= e.getLorange());

            if (overlaps)
                throw new RoySchedRangeOverlapException(titleId, lo, hi);
        }
    }

    // ─────────────── MAPPERS ───────────────

    private TitleResponse mapToResponse(Title t) {
        return TitleResponse.builder()
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

    private RoySchedResponse mapRoySchedToResponse(RoySched rs) {
        return RoySchedResponse.builder()
                .roySchedId(rs.getRoySchedId())
                .titleId(rs.getTitle() != null ? rs.getTitle().getTitleId() : null)
                .titleName(rs.getTitle() != null ? rs.getTitle().getTitle() : null)
                .lorange(rs.getLorange())
                .hirange(rs.getHirange())
                .royalty(rs.getRoyalty())
                .build();
    }
}