package com.sprint.Book_Partner_Application.publisher.controller;

import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.dto.ApiResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherCreateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherUpdateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.response.PublisherResponse;
import com.sprint.Book_Partner_Application.publisher.service.PublisherService;



import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    // ───────────── CREATE ─────────────
    @PostMapping
    public ApiResponse<PublisherResponse> createPublisher(
            @Valid @RequestBody PublisherCreateRequest request) {

        PublisherResponse response = publisherService.createPublisher(request);

        return ApiResponse.success("Publisher created successfully", response);
    }

    // ───────────── GET ALL (FILTER + PAGINATION) ─────────────
    @GetMapping
    public ApiResponse<PageResponse<PublisherResponse>> getAllPublishers(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String country,
            @PageableDefault(size = 10) Pageable pageable) {

        PageResponse<PublisherResponse> response =
                publisherService.getAllPublishers(city, state, country, pageable);

        return ApiResponse.success(response);
    }

    // ───────────── GET BY ID ─────────────
    @GetMapping("/{pubId}")
    public ApiResponse<PublisherResponse> getPublisherById(@PathVariable String pubId) {

        PublisherResponse response = publisherService.getPublisherById(pubId);

        return ApiResponse.success(response);
    }

    // ───────────── UPDATE ─────────────
    @PutMapping("/{pubId}")
    public ApiResponse<PublisherResponse> updatePublisher(
            @PathVariable String pubId,
            @Valid @RequestBody PublisherUpdateRequest request) {

        PublisherResponse response =
                publisherService.updatePublisher(pubId, request);

        return ApiResponse.success("Publisher updated successfully", response);
    }

    // ───────────── DELETE ─────────────
    @DeleteMapping("/{pubId}")
    public ApiResponse<Void> deletePublisher(@PathVariable String pubId) {

        publisherService.deletePublisher(pubId);

        return ApiResponse.successMessage("Publisher deleted successfully");
    }

    // ───────────── GET EMPLOYEES BY PUBLISHER ─────────────
    @GetMapping("/{pubId}/employees")
    public ApiResponse<List<EmployeeResponse>> getEmployeesByPublisher(
            @PathVariable String pubId) {

        List<EmployeeResponse> employees =
                publisherService.getEmployeesByPartner(pubId);

        return ApiResponse.success(employees);
    }

    // ───────────── GET TITLES BY PUBLISHER ─────────────
    @GetMapping("/{pubId}/titles")
    public ApiResponse<List<TitleResponse>> getTitlesByPublisher(
            @PathVariable String pubId) {

        List<TitleResponse> titles =
                publisherService.getProductsByPartner(pubId);

        return ApiResponse.success(titles);
    }
}