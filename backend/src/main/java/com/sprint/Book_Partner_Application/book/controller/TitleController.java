package com.sprint.Book_Partner_Application.book.controller;

import com.sprint.Book_Partner_Application.book.dto.request.TitleCreateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.TitleUpdateRequest;
import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.book.service.TitleService;
import com.sprint.Book_Partner_Application.dto.ApiResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/titles")
public class TitleController {

    @Autowired
    private TitleService titleService;

    public TitleController(TitleService titleService) {
        this.titleService = titleService;
    }

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<TitleResponse>> createTitle(
            @Valid @RequestBody TitleCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Title created successfully",
                        titleService.createTitle(request)));
    }

    // ================= GET ALL WITH PAGINATION =================
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TitleResponse>>> getAllTitles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<TitleResponse> result = titleService.getAllTitles(pageable);

        return ResponseEntity.ok(
                ApiResponse.success("Titles fetched successfully", result)
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TitleResponse>> getTitleById(@PathVariable String id) {

        return ResponseEntity.ok(
                ApiResponse.success("Title fetched successfully",
                        titleService.getTitleById(id))
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TitleResponse>> updateTitle(
            @PathVariable String id,
            @Valid @RequestBody TitleUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success("Title updated successfully",
                        titleService.updateTitle(id, request))
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTitle(@PathVariable String id) {

        titleService.deleteTitle(id);

        return ResponseEntity.ok(
                ApiResponse.successMessage("Title deleted successfully"));
    }
}