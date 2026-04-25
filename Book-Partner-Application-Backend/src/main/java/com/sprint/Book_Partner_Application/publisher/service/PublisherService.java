package com.sprint.Book_Partner_Application.publisher.service;


import com.sprint.Book_Partner_Application.book.dto.response.TitleResponse;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.response.EmployeeResponse;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherCreateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.request.PublisherUpdateRequest;
import com.sprint.Book_Partner_Application.publisher.dto.response.PublisherResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PublisherService {

    PublisherResponse createPublisher(PublisherCreateRequest request);

    PageResponse<PublisherResponse> getAllPublishers(Pageable pageable);

    PublisherResponse getPublisherById(String pubId);

    PublisherResponse updatePublisher(String pubId, PublisherUpdateRequest request);

    void deletePublisher(String pubId);

    List<EmployeeResponse> getEmployeesByPublisher(String pubId);

    List<TitleResponse> getTitlesByPublisher(String pubId);
}