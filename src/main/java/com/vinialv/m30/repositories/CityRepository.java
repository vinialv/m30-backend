package com.vinialv.m30.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinialv.m30.entities.City;

public interface CityRepository extends JpaRepository<City, Long> {
  List<City> findByStateId(Long state_id);
}
