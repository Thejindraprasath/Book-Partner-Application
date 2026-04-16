package com.sprint.Book_Partner_Application.book.service;

import com.sprint.Book_Partner_Application.author.dto.AuthorDTO;
import com.sprint.Book_Partner_Application.book.dto.RoySchedDTO;
import com.sprint.Book_Partner_Application.book.dto.TitleDTO;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TitleService {
    TitleDTO.Response createTitle(TitleDTO.Request request);
    PageResponse<TitleDTO.Response> getAllTitles(String type, String pubId, Double minPrice, Double maxPrice, Pageable pageable);

    TitleDTO.Response getTitleById(String titleId);

    TitleDTO.Response updateTitle(String titleId, TitleDTO.UpdateRequest request);

    void deleteTitle(String titleId);

    List<AuthorDTO.Response> getAuthorsByTitle(String titleId);

    List<RoySchedDTO.Response> getRoySchedsByTitle(String titleId);

    RoySchedDTO.Response createRoySched(RoySchedDTO.Request request);

    RoySchedDTO.Response updateRoySched(Long roySchedId, RoySchedDTO.Request request);

    void deleteRoySched(Long roySchedId);
}
