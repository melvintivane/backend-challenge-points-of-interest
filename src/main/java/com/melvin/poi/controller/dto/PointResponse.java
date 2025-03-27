package com.melvin.poi.controller.dto;

public record PointResponse(
     String name,
     Long x,
     Long y,
     Double distance
) {
}
