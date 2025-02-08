package com.vinialv.m30.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vinialv.m30.entities.State;
import com.vinialv.m30.repositories.StateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StateService {

  private final StateRepository repository;

    public List<State> findAll() {
    return repository.findAll();
  }

  public Optional<State> findById(Long id) {
    return repository.findById(id);
  }
}