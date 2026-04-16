
package com.sprint.Book_Partner_Application.author.service;

import com.sprint.Book_Partner_Application.author.dto.AuthorDTO;
import com.sprint.Book_Partner_Application.author.dto.TitleAuthorDTO;
import com.sprint.Book_Partner_Application.author.entity.Author;
import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.author.repository.AuthorRepository;
import com.sprint.Book_Partner_Application.author.repository.TitleAuthorRepository;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final TitleAuthorRepository titleAuthorRepository;

    // ─── CREATE AUTHOR ─────────────────────────────────────────────────────
    @Override
    public AuthorDTO.Response createAuthor(AuthorDTO.Request request) {

        // Duplicate check
        if (authorRepository.existsById(request.getAuId())) {
            throw new DuplicateResourceException("Author", "auId", request.getAuId());
        }

        // Validation
        if (request.getContract() != null && !(request.getContract() == 0 || request.getContract() == 1)) {
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

    // ─── GET ALL AUTHORS (FILTER + PAGINATION) ─────────────────────────────
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public PageResponse<AuthorDTO.Response> getAllAuthors(
            String city, String state, Integer contract, Pageable pageable) {

        if (contract != null && !(contract == 0 || contract == 1)) {
            throw new InvalidOperationException("Contract filter must be 0 or 1");
        }

        Page<Author> page = authorRepository.findWithFilters(city, state, contract, pageable);

        return PageResponse.from(page.map(this::mapToResponse));
    }

    // ─── GET AUTHOR BY ID ──────────────────────────────────────────────────
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public AuthorDTO.Response getAuthorById(String auId) {

        Author author = authorRepository.findById(auId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "auId", auId));

        return mapToResponse(author);
    }

    // ─── UPDATE AUTHOR ─────────────────────────────────────────────────────
    @Override
    public AuthorDTO.Response updateAuthor(String auId, AuthorDTO.UpdateRequest request) {

        Author author = authorRepository.findById(auId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "auId", auId));

        // Validations
        if (request.getContract() != null && !(request.getContract() == 0 || request.getContract() == 1)) {
            throw new BusinessValidationException("contract", "must be 0 or 1");
        }

        if (request.getZip() != null && request.getZip().length() != 5) {
            throw new BusinessValidationException("zip", "must be exactly 5 digits");
        }

        // Partial update
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

    // ─── DELETE AUTHOR ─────────────────────────────────────────────────────
    @Override
    public void deleteAuthor(String auId) {

        Author author = authorRepository.findById(auId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "auId", auId));

        // Check dependencies (optimized)
        boolean hasTitles = titleAuthorRepository.existsByAuId(auId);

        if (hasTitles) {
            throw new ResourceInUseException("Author", auId, "title associations");
        }

        authorRepository.delete(author);
    }

    // ─── GET TITLES BY AUTHOR ──────────────────────────────────────────────
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<TitleAuthorDTO.Response> getProductsByAuthor(String auId) {

        authorRepository.findById(auId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "auId", auId));

        List<TitleAuthor> list = titleAuthorRepository.findByAuId(auId);

        if (list.isEmpty()) {
            throw new InvalidOperationException("No titles found for Author '" + auId + "'");
        }

        return list.stream()
                .map(this::mapTitleAuthorToResponse)
                .collect(Collectors.toList());
    }

    // ─── MAPPERS ───────────────────────────────────────────────────────────
    private AuthorDTO.Response mapToResponse(Author a) {
        return AuthorDTO.Response.builder()
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

    private TitleAuthorDTO.Response mapTitleAuthorToResponse(TitleAuthor ta) {

        String authorName = (ta.getAuthor() != null)
                ? ta.getAuthor().getAuFname() + " " + ta.getAuthor().getAuLname()
                : ta.getAuId();

        String titleName = (ta.getTitle() != null)
                ? ta.getTitle().getTitle()
                : ta.getTitleId();

        return TitleAuthorDTO.Response.builder()
                .auId(ta.getAuId())
                .authorName(authorName)
                .titleId(ta.getTitleId())
                .titleName(titleName)
                .auOrd(ta.getAuOrd())
                .royaltyper(ta.getRoyaltyper())
                .build();
    }
}

