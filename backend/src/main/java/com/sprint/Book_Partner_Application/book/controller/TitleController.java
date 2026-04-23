package com.sprint.Book_Partner_Application.book.controller;

import com.sprint.Book_Partner_Application.book.dto.request.TitleCreateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.TitleUpdateRequest;
import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.book.service.TitleService;
import com.sprint.Book_Partner_Application.dto.ApiResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/titles")
public class TitleController {

    // Constructor Injection (Best Practice)
    private final TitleService titleService;

    public TitleController(TitleService titleService) {
        this.titleService = titleService;
    }

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<TitleResponse>> createTitle(
            @Valid @RequestBody TitleCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Title created",
                        titleService.createTitle(request)));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TitleResponse>>> getAllTitles(
            Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.success("Titles fetched",
                        titleService.getAllTitles(pageable))
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TitleResponse>> getTitleById(@PathVariable String id) {

        return ResponseEntity.ok(
                ApiResponse.success("Title fetched",
                        titleService.getTitleById(id))
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TitleResponse>> updateTitle(
            @PathVariable String id,
            @RequestBody TitleUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success("Title updated",
                        titleService.updateTitle(id, request))
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTitle(@PathVariable String id) {

        titleService.deleteTitle(id);

        return ResponseEntity.ok(
                ApiResponse.successMessage("Title deleted"));
    }
}