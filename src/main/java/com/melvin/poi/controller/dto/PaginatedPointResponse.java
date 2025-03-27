package com.melvin.poi.controller.dto;

import java.util.List;

public record PaginatedPointResponse(
     List<PointResponse> content,
     PaginationMetadata metadata
){}
