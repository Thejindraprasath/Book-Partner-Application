package com.sprint.Book_Partner_Application.author.service;



import com.sprint.Book_Partner_Application.author.dto.AuthorDTO;
import com.sprint.Book_Partner_Application.author.dto.TitleAuthorDTO;
//import com.sprint.Book_Partner_Application.dto.AuthorDTO;
import com.sprint.Book_Partner_Application.dto.PageResponse;
//import com.sprint.Book_Partner_Application.dto.TitleAuthorDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {
    AuthorDTO.Response createAuthor(AuthorDTO.Request request);
    PageResponse<AuthorDTO.Response> getAllAuthors(String city, String state, Integer contract, Pageable pageable);
    AuthorDTO.Response getAuthorById(String auId);
    AuthorDTO.Response updateAuthor(String auId, AuthorDTO.UpdateRequest request);
    void deleteAuthor(String auId);
    List<TitleAuthorDTO.Response> getProductsByAuthor(String auId);
}