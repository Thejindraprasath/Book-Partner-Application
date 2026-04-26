package com.sprint.Book_Partner_Application.author.service;

import com.sprint.Book_Partner_Application.author.dto.request.AuthorCreateRequest;
import com.sprint.Book_Partner_Application.author.dto.request.AuthorUpdateRequest;
import com.sprint.Book_Partner_Application.author.dto.response.AuthorResponse;
import com.sprint.Book_Partner_Application.author.dto.response.TitleAuthorResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {

    AuthorResponse createAuthor(AuthorCreateRequest request);

    PageResponse<AuthorResponse> getAllAuthors(Pageable pageable);

    AuthorResponse getAuthorById(String auId);

    AuthorResponse updateAuthor(String auId, AuthorUpdateRequest request);

    void deleteAuthor(String auId);

    List<TitleAuthorResponse> getProductsByAuthor(String auId);
}