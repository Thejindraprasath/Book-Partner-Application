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

import java.util.List;
import java.util.stream.Collectors;

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

        Author author = Author.builder()
                .auId(request.getAuId())
                .auLname(request.getAuLname())
                .auFname(request.getAuFname())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zip(request.getZip())
                .contract(request.getContract())
                .build();

        return mapToResponse(authorRepository.save(author));
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

        return PageResponse.from(page.map(this::mapToResponse));
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

        return mapToResponse(authorRepository.save(author));
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

        return list.stream()
                .map(this::mapTitleAuthorToResponse)
                .collect(Collectors.toList());
    }

    // ─── MAPPERS ───────────────────────────────
    private AuthorResponse mapToResponse(Author a) {

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
    }

    private TitleAuthorResponse mapTitleAuthorToResponse(TitleAuthor ta) {

        String authorName = (ta.getAuthor() != null)
                ? ta.getAuthor().getAuFname() + " " + ta.getAuthor().getAuLname()
                : ta.getAuId();

        String titleName = (ta.getTitle() != null)
                ? ta.getTitle().getTitle()
                : ta.getTitleId();

        return TitleAuthorResponse.builder()
                .auId(ta.getAuId())
                .authorName(authorName)
                .titleId(ta.getTitleId())
                .titleName(titleName)
                .auOrd(ta.getAuOrd())
                .royaltyper(ta.getRoyaltyper())
                .build();
    }
}