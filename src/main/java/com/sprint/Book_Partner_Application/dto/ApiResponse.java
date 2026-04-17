package com.sprint.Book_Partner_Application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Object errors;
    private LocalDateTime timestamp;

    // ================= SUCCESS =================

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .errors(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return success("Request successful", data);
    }

    public static <T> ApiResponse<T> successMessage(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(null)
                .errors(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ================= FAILURE =================

    public static <T> ApiResponse<T> failure(String message, Object errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> failure(String message) {
        return failure(message, null);
    }
}
