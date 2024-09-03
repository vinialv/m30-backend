package com.vinialv.m30.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinialv.m30.entities.ProjectImage;
import com.vinialv.m30.services.ProjectImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/project-image")
public class ProjectImageController {

  private final ProjectImageService service;

  @GetMapping
  public List<ProjectImage> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectImage> findById(@PathVariable("id") Long id) {
    Optional<ProjectImage> projectImage = service.findById(id);
    return projectImage.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  
  @GetMapping("/project/{projectId}")
  public List<ProjectImage> findByProject(@PathVariable("projectId") Long id) {
      return service.findByProjectId(id);
  }

  @PostMapping
  public void save(@RequestBody ProjectImage projectImage) {
    service.createImage(projectImage);
  }

  @PutMapping("/{id}")
  public void update(@PathVariable("id") Long id, @RequestBody ProjectImage projectImage) {
    service.updateImage(id, projectImage);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    service.deleteImage(id);
  }

}
