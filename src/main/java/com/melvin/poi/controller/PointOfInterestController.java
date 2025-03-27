package com.melvin.poi.controller;

import com.melvin.poi.controller.dto.CreateOfInterest;
import com.melvin.poi.controller.dto.PaginatedPointResponse;
import com.melvin.poi.entity.PointOfInterest;
import com.melvin.poi.service.PointOfInterestService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class PointOfInterestController {

   private final PointOfInterestService service;

   public PointOfInterestController(PointOfInterestService service) {
      this.service = service;
   }

   @PostMapping("/points-of-interest")
   public ResponseEntity<Void> createPointOfInterest(@RequestBody CreateOfInterest body) {
      service.save(body);

      return ResponseEntity.ok().build();
   }

   @GetMapping("/points-of-interest")
   public ResponseEntity<Page<PointOfInterest>> getPointsOfInterest(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

      var body = service.findAll(page, pageSize);

      return ResponseEntity.ok(body);
   }

   @GetMapping("/near-me")
   public ResponseEntity<List<PaginatedPointResponse>> nearMe(@RequestParam(name = "x") Long x,
                                                              @RequestParam(name = "y") Long y,
                                                              @RequestParam(name = "dmax") Long dmax,
                                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

      var body = service.findNearMe(x, y, dmax, page, pageSize);

      return ResponseEntity.ok(Collections.singletonList(body));
   }
}
