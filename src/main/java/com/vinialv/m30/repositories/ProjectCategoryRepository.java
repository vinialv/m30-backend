package com.vinialv.m30.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinialv.m30.entities.ProjectCategory;

public interface ProjectCategoryRepository extends JpaRepository<ProjectCategory, Long> {
  
}
