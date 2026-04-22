package com.sprint.Book_Partner_Application.publisher.controller;

import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.dto.ApiResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherCreateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherUpdateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.response.PublisherResponse;
import com.sprint.Book_Partner_Application.publisher.service.PublisherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    // ───────────── CREATE ─────────────
    @PostMapping
    public ResponseEntity<ApiResponse<PublisherResponse>> createPublisher(
            @Valid @RequestBody PublisherCreateRequest request) {

        PublisherResponse response = publisherService.createPublisher(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Publisher created successfully", response));
    }

    // ───────────── GET ALL ─────────────
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PublisherResponse>>> getAllPublishers(@PageableDefault(size = 10) Pageable pageable) {

        PageResponse<PublisherResponse> response =
                publisherService.getAllPublishers(pageable);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ───────────── GET BY ID ─────────────
    @GetMapping("/{pubId}")
    public ResponseEntity<ApiResponse<PublisherResponse>> getPublisherById(
            @PathVariable String pubId) {

        PublisherResponse response = publisherService.getPublisherById(pubId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ───────────── UPDATE ─────────────
    @PutMapping("/{pubId}")
    public ResponseEntity<ApiResponse<PublisherResponse>> updatePublisher(
            @PathVariable String pubId,
            @Valid @RequestBody PublisherUpdateRequest request) {

        PublisherResponse response =
                publisherService.updatePublisher(pubId, request);

        return ResponseEntity.ok(
                ApiResponse.success("Publisher updated successfully", response)
        );
    }

    // ───────────── DELETE ─────────────
    @DeleteMapping("/{pubId}")
    public ResponseEntity<ApiResponse<Void>> deletePublisher(
            @PathVariable String pubId) {

        publisherService.deletePublisher(pubId);

        return ResponseEntity.ok(
                ApiResponse.successMessage("Publisher deleted successfully")
        );
    }

    // ───────────── GET EMPLOYEES ─────────────
    @GetMapping("/{pubId}/employees")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getEmployeesByPublisher(
            @PathVariable String pubId) {

        List<EmployeeResponse> employees =
                publisherService.getEmployeesByPublisher(pubId);

        return ResponseEntity.ok(ApiResponse.success(employees));
    }

    // ───────────── GET TITLES ─────────────
    @GetMapping("/{pubId}/titles")
    public ResponseEntity<ApiResponse<List<TitleResponse>>> getTitlesByPublisher(
            @PathVariable String pubId) {

        List<TitleResponse> titles =
                publisherService.getProductsByPublisher(pubId);

        return ResponseEntity.ok(ApiResponse.success(titles));
    }
}