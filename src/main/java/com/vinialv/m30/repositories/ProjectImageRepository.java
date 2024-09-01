package com.vinialv.m30.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinialv.m30.entities.ProjectImage;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
  List<ProjectImage> findByProjectId(Long projectId);
}