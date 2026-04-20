package com.sprint.Book_Partner_Application.author.service;

import com.sprint.Book_Partner_Application.author.dto.request.AuthorCreateRequest;
import com.sprint.Book_Partner_Application.author.dto.request.AuthorUpdateRequest;
import com.sprint.Book_Partner_Application.author.dto.response.AuthorResponse;
import com.sprint.Book_Partner_Application.author.dto.response.TitleAuthorResponse;
import com.sprint.Book_Partner_Application.author.entity.Author;
import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.author.repository.AuthorRepository;
import com.sprint.Book_Partner_Application.author.repository.TitleAuthorRepository;
import com.sprint.Book_Partner_Application.author.exception.*;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.exception.*;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TitleAuthorRepository titleAuthorRepository;

    // ─── CREATE AUTHOR ───────────────────────────────
    @Override
    public AuthorResponse createAuthor(AuthorCreateRequest request) {

        if (authorRepository.existsById(request.getAuId())) {
            throw new AuthorAlreadyExistsException(request.getAuId());
        }

        if (request.getContract() != null &&
                !(request.getContract() == 0 || request.getContract() == 1)) {
            throw new BusinessValidationException("contract", "must be 0 or 1");
        }

        // SIMPLE OBJECT CREATION (no builder)
        Author author = new Author();
        author.setAuId(request.getAuId());
        author.setAuLname(request.getAuLname());
        author.setAuFname(request.getAuFname());
        author.setPhone(request.getPhone());
        author.setAddress(request.getAddress());
        author.setCity(request.getCity());
        author.setState(request.getState());
        author.setZip(request.getZip());
        author.setContract(request.getContract());

        Author saved = authorRepository.save(author);

        return mapToResponse(saved);
    }

    // ─── GET ALL AUTHORS ───────────────────────────────
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public PageResponse<AuthorResponse> getAllAuthors(
            String city,
            String state,
            Integer contract,
            Pageable pageable
    ) {

        if (contract != null && !(contract == 0 || contract == 1)) {
            throw new InvalidOperationException("Contract filter must be 0 or 1");
        }

        Page<Author> page =
                authorRepository.findWithFilters(city, state, contract, pageable);

        // convert manually (no stream)
        List<AuthorResponse> responseList = new ArrayList<>();

        for (Author a : page.getContent()) {
            responseList.add(mapToResponse(a));
        }

        PageResponse<AuthorResponse> response = new PageResponse<>();

        response.setContent(responseList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());

        return response;
    }

    // ─── GET AUTHOR BY ID ───────────────────────────────
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public AuthorResponse getAuthorById(String auId) {

        Author author = authorRepository.findById(auId)
                .orElseThrow(() -> new AuthorNotFoundException(auId));

        return mapToResponse(author);
    }

    // ─── UPDATE AUTHOR ───────────────────────────────
    @Override
    public AuthorResponse updateAuthor(String auId, AuthorUpdateRequest request) {

        Author author = authorRepository.findById(auId)
                .orElseThrow(() -> new AuthorNotFoundException(auId));

        if (request.getContract() != null &&
                !(request.getContract() == 0 || request.getContract() == 1)) {
            throw new BusinessValidationException("contract", "must be 0 or 1");
        }

        if (request.getZip() != null && request.getZip().length() != 5) {
            throw new BusinessValidationException("zip", "must be exactly 5 digits");
        }

        if (request.getAuLname() != null) author.setAuLname(request.getAuLname());
        if (request.getAuFname() != null) author.setAuFname(request.getAuFname());
        if (request.getPhone() != null) author.setPhone(request.getPhone());
        if (request.getAddress() != null) author.setAddress(request.getAddress());
        if (request.getCity() != null) author.setCity(request.getCity());
        if (request.getState() != null) author.setState(request.getState());
        if (request.getZip() != null) author.setZip(request.getZip());
        if (request.getContract() != null) author.setContract(request.getContract());

        Author updated = authorRepository.save(author);

        return mapToResponse(updated);
    }

    // ─── DELETE AUTHOR ───────────────────────────────
    @Override
    public void deleteAuthor(String auId) {

        Author author = authorRepository.findById(auId)
                .orElseThrow(() -> new AuthorNotFoundException(auId));

        boolean hasTitles = titleAuthorRepository.existsByAuId(auId);

        if (hasTitles) {
            throw new AuthorHasActiveTitlesException(auId);
        }

        authorRepository.delete(author);
    }

    // ─── GET TITLES BY AUTHOR ───────────────────────────────
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<TitleAuthorResponse> getProductsByAuthor(String auId) {

        authorRepository.findById(auId)
                .orElseThrow(() -> new AuthorNotFoundException(auId));

        List<TitleAuthor> list = titleAuthorRepository.findByAuId(auId);

        if (list.isEmpty()) {
            throw new InvalidOperationException("No titles found for Author '" + auId + "'");
        }

        // manual conversion (no stream)
        List<TitleAuthorResponse> responseList = new ArrayList<>();

        for (TitleAuthor ta : list) {
            responseList.add(mapTitleAuthorToResponse(ta));
        }

        return responseList;
    }

    // ─── MAPPERS ───────────────────────────────

    private AuthorResponse mapToResponse(Author a) {

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

        return res;
    }

    private TitleAuthorResponse mapTitleAuthorToResponse(TitleAuthor ta) {

        String authorName;
        if (ta.getAuthor() != null) {
            authorName = ta.getAuthor().getAuFname() + " " + ta.getAuthor().getAuLname();
        } else {
            authorName = ta.getAuId();
        }

        String titleName;
        if (ta.getTitle() != null) {
            titleName = ta.getTitle().getTitle();
        } else {
            titleName = ta.getTitleId();
        }

        TitleAuthorResponse res = new TitleAuthorResponse();
        res.setAuId(ta.getAuId());
        res.setAuthorName(authorName);
        res.setTitleId(ta.getTitleId());
        res.setTitleName(titleName);
        res.setAuOrd(ta.getAuOrd());
        res.setRoyaltyper(ta.getRoyaltyper());

        return res;
    }
}