package com.vinialv.m30.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinialv.m30.entities.State;
import com.vinialv.m30.services.StateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/states")
public class StateController {

  private final StateService service;

  @GetMapping
  public List<State> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<State> findById(@PathVariable("id") Long id) {
    Optional<State> state = service.findById(id);
    return state.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

}
