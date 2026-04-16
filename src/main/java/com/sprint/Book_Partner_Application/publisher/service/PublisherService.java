package com.sprint.Book_Partner_Application.publisher.service;

import com.sprint.Book_Partner_Application.book.dto.TitleDTO;
import com.sprint.Book_Partner_Application.dto.PageResponse;
import com.sprint.Book_Partner_Application.employee.dto.EmployeeDTO;
import com.sprint.Book_Partner_Application.publisher.dto.PublisherDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PublisherService {
    PublisherDTO.Response createPublisher(PublisherDTO.Request request);
    PageResponse<PublisherDTO.Response> getAllPublishers(String city, String state, String country, Pageable pageable);
    PublisherDTO.Response getPublisherById(String pubId);
    PublisherDTO.Response updatePublisher(String pubId, PublisherDTO.UpdateRequest request);
    void deletePublisher(String pubId);
    List<EmployeeDTO.Response> getEmployeesByPartner(String pubId);
    List<TitleDTO.Response> getProductsByPartner(String pubId);
}
