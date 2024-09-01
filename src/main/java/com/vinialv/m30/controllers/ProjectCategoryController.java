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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vinialv.m30.entities.ProjectCategory;
import com.vinialv.m30.services.ProjectCategoryService;

@RestController
@RequestMapping("/v1/project-category")
public class ProjectCategoryController {

  private final ProjectCategoryService service;

  public ProjectCategoryController(ProjectCategoryService service) {
    this.service = service;
  }

  @GetMapping
  public List<ProjectCategory> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectCategory> findById(@PathVariable("id") Long id) {
    Optional<ProjectCategory> projectCategory = service.findById(id);
    return projectCategory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public void save(@RequestBody ProjectCategory projectCategory) {
    service.createProjectCategory(projectCategory);
  }

  @PutMapping("/{id}")
  public void update(@PathVariable("id") Long id, @RequestBody ProjectCategory projectCategory) {
    service.updateProjectCategory(id, projectCategory);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    service.deleteProjectCategory(id);
  }
}
