package com.vinialv.m30.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ProjectCategory findById(@PathVariable("id") Long id) {
    return service.findById(id);
  }

  @PostMapping
  public void save(@RequestBody ProjectCategory projectCategory) {
    service.save(projectCategory);
  }

  @PutMapping("/{id}")
  public void update(@PathVariable("id") Long id, @RequestBody ProjectCategory projectCategory) {
    service.update(id, projectCategory);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    service.delete(id);
  }
}
