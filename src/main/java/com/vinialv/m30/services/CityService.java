package com.vinialv.m30.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vinialv.m30.entities.City;
import com.vinialv.m30.repositories.CityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CityService {

  private final CityRepository repository;

    public List<City> findAll() {
    return repository.findAll();
  }

  public Optional<City> findById(Long id) {
    return repository.findById(id);
  }

  public List<City> findByStateId(Long state_id) {
    return repository.findByStateId(state_id);
  }
}