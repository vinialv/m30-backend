package com.vinialv.m30.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinialv.m30.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  Optional<Project> findByName(String name);
}
