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

import com.vinialv.m30.entities.Project;
import com.vinialv.m30.services.ProjectService;

@RestController
@RequestMapping("/v1/project")
public class ProjectController {

  private final ProjectService service;

  public ProjectController(ProjectService service) {
    this.service = service;
  }

  @GetMapping
  public List<Project> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Project> findById(@PathVariable("id") Long id) {
    Optional<Project> project = service.findById(id);
    return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public void save(@RequestBody Project project) {
    service.createProject(project);
  }

  @PutMapping("/{id}")
  public void update(@PathVariable("id") Long id, @RequestBody Project project) {
    service.updateProject(id, project);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    service.deleteProject(id);
  }

}
