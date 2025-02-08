package com.vinialv.m30.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinialv.m30.entities.City;
import com.vinialv.m30.services.CityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cities")
public class CityController {

  private final CityService service;

  @GetMapping
  public List<City> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<City> findById(@PathVariable("id") Long id) {
    Optional<City> city = service.findById(id);
    return city.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/state/{state_id}")
  public ResponseEntity<List<City>> findByStateId(@PathVariable("state_id") Long state_id) {
      List<City> cities = service.findByStateId(state_id);
      if (cities.isEmpty()) {
          return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(cities);
  }

}
