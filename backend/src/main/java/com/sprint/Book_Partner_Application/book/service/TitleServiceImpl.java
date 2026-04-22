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
import com.sprint.Book_Partner_Application.publisher.exception.PublisherNotFoundException;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TitleServiceImpl implements TitleService {

    private final TitleRepository titleRepository;
    private final PublisherRepository publisherRepository;
    private final TitleAuthorRepository titleAuthorRepository;
    private final RoySchedRepository roySchedRepository;
    private final SaleRepository saleRepository;

    public TitleServiceImpl(TitleRepository titleRepository,
                            PublisherRepository publisherRepository,
                            TitleAuthorRepository titleAuthorRepository,
                            RoySchedRepository roySchedRepository,
                            SaleRepository saleRepository) {
        this.titleRepository = titleRepository;
        this.publisherRepository = publisherRepository;
        this.titleAuthorRepository = titleAuthorRepository;
        this.roySchedRepository = roySchedRepository;
        this.saleRepository = saleRepository;
    }

    // ================= CREATE =================
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
        if (request.getPubId() != null) {
            publisher = publisherRepository.findById(request.getPubId())
                    .orElseThrow(() -> new PublisherNotFoundException(request.getPubId()));
        }

        Title title = new Title();
        title.setTitleId(request.getTitleId());
        title.setTitle(request.getTitle());
        title.setType(request.getType());
        title.setPublisher(publisher);
        title.setPrice(request.getPrice());
        title.setAdvance(request.getAdvance());
        title.setRoyalty(request.getRoyalty());
        title.setYtdSales(request.getYtdSales());
        title.setNotes(request.getNotes());
        title.setPubdate(request.getPubdate());

        Title saved = titleRepository.save(title);

        return mapToResponse(saved);
    }

    // ================= READ ALL =================
    @Override
    @Transactional(readOnly = true)
    public PageResponse<TitleResponse> getAllTitles(Pageable pageable) {

        Page<Title> page = titleRepository.findWithFilters(pageable);

        List<TitleResponse> responseList = new ArrayList<>();

        for (Title t : page.getContent()) {
            responseList.add(mapToResponse(t));
        }

        return PageResponse.from(page.map(this::mapToResponse));
    }

    // ================= READ ONE =================
    @Override
    @Transactional(readOnly = true)
    public TitleResponse getTitleById(String titleId) {

        Title title = titleRepository.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));

        return mapToResponse(title);
    }

    // ================= UPDATE =================
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

    // ================= DELETE =================
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

    // ================= AUTHORS =================
    @Override
    public List<AuthorResponse> getAuthorsByTitle(String titleId) {

        titleRepository.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));

        List<TitleAuthor> list = titleAuthorRepository.findByTitleId(titleId);
        List<AuthorResponse> result = new ArrayList<>();

        for (TitleAuthor ta : list) {

            if (ta.getAuthor() != null) {

                var a = ta.getAuthor();

                AuthorResponse res = new AuthorResponse();
                res.setAuId(a.getAuId());
                res.setAuLname(a.getAuLname());
                res.setAuFname(a.getAuFname());
                res.setPhone(a.getPhone());
                res.setAddress(a.getAddress());
                res.setCity(a.getCity());
                res.setState(a.getState());
                res.setZip(a.getZip());
                res.setContract(a.getContract());

                result.add(res);
            }
        }

        return result;
    }

    // ================= CREATE ROYSCHED =================
    @Override
    public RoySchedResponse createRoySched(RoySchedCreateRequest request) {

        Title title = titleRepository.findById(request.getTitleId())
                .orElseThrow(() -> new TitleNotFoundException(request.getTitleId()));

        validateRange(request.getLorange(), request.getHirange(), request.getTitleId(), null);

        if (request.getRoyalty() != null &&
                (request.getRoyalty() < 0 || request.getRoyalty() > 100))
            throw new InvalidRoyaltyException(request.getRoyalty());

        RoySched rs = new RoySched();
        rs.setTitle(title);
        rs.setLorange(request.getLorange());
        rs.setHirange(request.getHirange());
        rs.setRoyalty(request.getRoyalty());

        RoySched saved = roySchedRepository.save(rs);

        return mapRoySchedToResponse(saved);
    }

    // ================= UPDATE ROYSCHED =================
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

    // ================= VALIDATION =================
    private void validateRange(Integer lo, Integer hi, String titleId, Long currentId) {

        if (lo != null && hi != null) {

            if (lo >= hi)
                throw new InvalidRoySchedRangeException(lo, hi);

            List<RoySched> list = roySchedRepository.findByTitle_TitleId(titleId);

            for (RoySched e : list) {

                if (e.getRoySchedId().equals(currentId)) {
                    continue;
                }

                if (lo <= e.getHirange() && hi >= e.getLorange()) {
                    throw new RoySchedRangeOverlapException(titleId, lo, hi);
                }
            }
        }
    }

    // ================= MAPPERS =================
    private TitleResponse mapToResponse(Title t) {

        TitleResponse res = new TitleResponse();

        res.setTitleId(t.getTitleId());
        res.setTitle(t.getTitle());
        res.setType(t.getType());
        res.setPubId(t.getPublisher() != null ? t.getPublisher().getPubId() : null);
        res.setPubName(t.getPublisher() != null ? t.getPublisher().getPubName() : null);
        res.setPrice(t.getPrice());
        res.setAdvance(t.getAdvance());
        res.setRoyalty(t.getRoyalty());
        res.setYtdSales(t.getYtdSales());
        res.setNotes(t.getNotes());
        res.setPubdate(t.getPubdate());

        return res;
    }

    private RoySchedResponse mapRoySchedToResponse(RoySched rs) {

        RoySchedResponse res = new RoySchedResponse();

        res.setRoySchedId(rs.getRoySchedId());
        res.setTitleId(rs.getTitle() != null ? rs.getTitle().getTitleId() : null);
        res.setTitleName(rs.getTitle() != null ? rs.getTitle().getTitle() : null);
        res.setLorange(rs.getLorange());
        res.setHirange(rs.getHirange());
        res.setRoyalty(rs.getRoyalty());

        return res;
    }
}