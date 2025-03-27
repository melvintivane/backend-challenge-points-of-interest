package com.melvin.poi.service;

import com.melvin.poi.controller.dto.CreateOfInterest;
import com.melvin.poi.controller.dto.PaginatedPointResponse;
import com.melvin.poi.controller.dto.PaginationMetadata;
import com.melvin.poi.controller.dto.PointResponse;
import com.melvin.poi.entity.PointOfInterest;
import com.melvin.poi.repository.PointOfInterestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointOfInterestService {

   private final PointOfInterestRepository repository;

   public PointOfInterestService(PointOfInterestRepository pointOfInterestRepository) {
      this.repository = pointOfInterestRepository;
   }

   public void save(CreateOfInterest body) {
      repository.save(new PointOfInterest(body.name(), body.x(), body.y()));
   }

   public Page<PointOfInterest> findAll(int page, int pageSize) {
      return repository.findAll(PageRequest.of(page, pageSize));
   }

   public PaginatedPointResponse findNearMe(long x, long y, long dmax, int page, int pageSize) {

      var xMin = x - dmax;
      var xMax = x + dmax;
      var yMin = y - dmax;
      var yMax = y + dmax;

      var points = repository.findNearMe(xMin, xMax, yMin, yMax, PageRequest.of(page, pageSize));

      List<PointResponse> content = points.getContent()
           .stream()
           .filter(p -> distanceBetweenTwoPoints(x, y, p.getX(), p.getY()) <= dmax)
           .map(p -> new PointResponse(
                p.getName(),
                p.getX(),
                p.getY(),
                distanceBetweenTwoPoints(x, y, p.getX(), p.getY())
           ))
           .toList();

      PaginationMetadata metadata = new PaginationMetadata(
           points.getTotalPages(),
           points.getTotalElements(),
           points.getPageable().getPageSize(),
           points.getPageable().getPageNumber()
      );

      return new PaginatedPointResponse(content, metadata);
   }

   public Double distanceBetweenTwoPoints(Long x1, Long y1, Long x2, Long y2) {
      return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
   }
}
