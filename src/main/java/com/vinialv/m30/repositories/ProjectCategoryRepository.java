package com.vinialv.m30.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vinialv.m30.entities.ProjectCategory;

public interface ProjectCategoryRepository extends JpaRepository<ProjectCategory, Long> {
  Optional<ProjectCategory> findByDescription(String description);
  Page<ProjectCategory> findAll(Pageable pageable);
}
