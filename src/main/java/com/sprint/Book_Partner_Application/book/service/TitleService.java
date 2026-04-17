package com.sprint.Book_Partner_Application.book.service;

import com.sprint.Book_Partner_Application.book.dto.request.TitleCreateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.TitleUpdateRequest;
import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface TitleService {

    TitleResponse createTitle(TitleCreateRequest request);

    PageResponse<TitleResponse> getAllTitles(
            String type,
            String pubId,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    );

    TitleResponse getTitleById(String titleId);

    TitleResponse updateTitle(String titleId, TitleUpdateRequest request);

    void deleteTitle(String titleId);
}