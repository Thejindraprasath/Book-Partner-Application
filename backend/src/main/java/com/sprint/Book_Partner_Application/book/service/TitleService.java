package com.sprint.Book_Partner_Application.book.service;

import com.sprint.Book_Partner_Application.author.dto.response.AuthorResponse;
import com.sprint.Book_Partner_Application.book.dto.request.RoySchedCreateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.RoySchedUpdateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.TitleCreateRequest;
import com.sprint.Book_Partner_Application.book.dto.request.TitleUpdateRequest;
import com.sprint.Book_Partner_Application.book.dto.response.RoySchedResponse;
import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;

import com.sprint.Book_Partner_Application.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TitleService {

    TitleResponse createTitle(TitleCreateRequest request);

    PageResponse<TitleResponse> getAllTitles(
            Pageable pageable
    );

    TitleResponse getTitleById(String titleId);

    TitleResponse updateTitle(String titleId, TitleUpdateRequest request);

    void deleteTitle(String titleId);

    RoySchedResponse updateRoySched(Long roySchedId, RoySchedUpdateRequest request);

    List<AuthorResponse> getAuthorsByTitle(String titleId);

    RoySchedResponse createRoySched(RoySchedCreateRequest request);
}