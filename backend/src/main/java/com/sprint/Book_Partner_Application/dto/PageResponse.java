package com.sprint.Book_Partner_Application.dto;

import org.springframework.data.domain.Page;

import java.util.List;

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

    //  No-Args Constructor
    public PageResponse() {}

    //  All-Args Constructor
    public PageResponse(List<T> content, int pageNumber, int pageSize,
                        int numberOfElements, long totalElements,
                        int totalPages, boolean last, boolean first, String sort) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.numberOfElements = numberOfElements;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
        this.first = first;
        this.sort = sort;
    }

    // ================= GETTERS & SETTERS =================

    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }

    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public int getNumberOfElements() { return numberOfElements; }
    public void setNumberOfElements(int numberOfElements) { this.numberOfElements = numberOfElements; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }

    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }

    // ================= STATIC METHODS =================

    // Same type (no conversion needed)
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst(),
                page.getSort().toString()
        );
    }

    //  Different type (Entity → DTO)
    public static <T, R> PageResponse<R> from(Page<T> page, List<R> content) {
        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst(),
                page.getSort().toString()
        );
    }
}