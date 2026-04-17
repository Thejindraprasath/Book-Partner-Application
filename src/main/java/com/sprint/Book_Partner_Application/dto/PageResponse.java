package com.sprint.Book_Partner_Application.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

    private List<T> content;

    private int pageNumber;
    private int pageSize;

    private int numberOfElements;

    private long totalElements;
    private int totalPages;

    private boolean last;
    private boolean first;

    private String sort;

    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .sort(page.getSort().toString())
                .build();
    }
}