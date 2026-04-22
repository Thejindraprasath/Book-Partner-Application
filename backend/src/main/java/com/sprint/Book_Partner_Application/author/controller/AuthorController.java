package com.sprint.Book_Partner_Application.author.controller;

import com.sprint.Book_Partner_Application.author.dto.request.AuthorCreateRequest;
import com.sprint.Book_Partner_Application.author.dto.request.AuthorUpdateRequest;
import com.sprint.Book_Partner_Application.author.dto.response.AuthorResponse;
import com.sprint.Book_Partner_Application.author.dto.response.TitleAuthorResponse;
import com.sprint.Book_Partner_Application.author.service.AuthorService;
import com.sprint.Book_Partner_Application.dto.ApiResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private AuthorService authorService;

    //  MANUAL CONSTRUCTOR (replaces @RequiredArgsConstructor)
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // ─── CREATE AUTHOR ─────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<AuthorResponse>> createAuthor(
            @Valid @RequestBody AuthorCreateRequest request) {

        AuthorResponse response = authorService.createAuthor(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Author created successfully", response));
    }

    // ─── GET ALL AUTHORS ───────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AuthorResponse>>> getAllAuthors(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "auLname") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort;
        if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<AuthorResponse> result =
                authorService.getAllAuthors(pageable);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // ─── GET AUTHOR BY ID ─────────────────────────
    @GetMapping("/{auId}")
    public ResponseEntity<ApiResponse<AuthorResponse>> getAuthorById(
            @PathVariable String auId) {

        AuthorResponse response = authorService.getAuthorById(auId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ─── UPDATE AUTHOR ────────────────────────────
    @PutMapping("/{auId}")
    public ResponseEntity<ApiResponse<AuthorResponse>> updateAuthor(
            @PathVariable String auId,
            @Valid @RequestBody AuthorUpdateRequest request) {

        AuthorResponse response = authorService.updateAuthor(auId, request);

        return ResponseEntity.ok(
                ApiResponse.success("Author updated successfully", response)
        );
    }

    // ─── DELETE AUTHOR ────────────────────────────
    @DeleteMapping("/{auId}")
    public ResponseEntity<ApiResponse<Void>> deleteAuthor(
            @PathVariable String auId) {

        authorService.deleteAuthor(auId);

        return ResponseEntity.ok(
                ApiResponse.successMessage("Author deleted successfully")
        );
    }

    // ─── GET TITLES BY AUTHOR ─────────────────────
    @GetMapping("/{auId}/titles")
    public ResponseEntity<ApiResponse<List<TitleAuthorResponse>>> getTitlesByAuthor(
            @PathVariable String auId) {

        List<TitleAuthorResponse> list =
                authorService.getProductsByAuthor(auId);

        return ResponseEntity.ok(ApiResponse.success(list));
    }
}