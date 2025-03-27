package com.melvin.poi.controller.dto;

public record PaginationMetadata(
     int totalPages,
     long totalElements,
     int pageSize,
     int pageNumber
) {
}
