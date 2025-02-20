package com.vinialv.m30.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vinialv.m30.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  Optional<Project> findByName(String name);
  Page<Project> findByVisibilityAndNameContaining(String visibility, String name, Pageable pageable);
  Page<Project> findByVisibility(String visibility, Pageable pageable);
  Page<Project> findByNameContaining(String name, Pageable pageable);
  Page<Project> findAll(Pageable pageable);
}
