package com.melvin.poi.controller;

import com.melvin.poi.controller.dto.CreateOfInterest;
import com.melvin.poi.entity.PointOfInterest;
import com.melvin.poi.repository.PointOfInterestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PointOfInterestController {

   private final PointOfInterestRepository repository;

   public PointOfInterestController(PointOfInterestRepository pointOfInterestRepository) {
      this.repository = pointOfInterestRepository;
   }

   @PostMapping("/points-of-interest")
   public ResponseEntity<Void> createPointOfInterest(@RequestBody CreateOfInterest body) {

      repository.save(new PointOfInterest(body.name(), body.x(), body.y()));

      return ResponseEntity.ok().build();
   }

   @GetMapping("/points-of-interest")
   public ResponseEntity<Page<PointOfInterest>> getPointsOfInterest(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

      var body = repository.findAll(PageRequest.of(page, pageSize));

      return ResponseEntity.ok(body);
   }

   @GetMapping("/near-me")
   public ResponseEntity<List<PointOfInterest>> nearMe(@RequestParam(name = "x") Long x,
                                                       @RequestParam(name = "y") Long y,
                                                       @RequestParam(name = "dmax") Long dmax) {

      var xMin = x - dmax;
      var xMax = x + dmax;
      var yMin = y - dmax;
      var yMax = y + dmax;

      var body = repository.findNearMe(xMin, xMax, yMin, yMax)
           .stream()
           .filter(p -> distanceBetweenTwoPoints(x, y, p.getX(), p.getY()) <= dmax)
           .toList();

      return ResponseEntity.ok(body);
   }

   public Double distanceBetweenTwoPoints(Long x1, Long y1, Long x2, Long y2) {
      return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
   }
}
