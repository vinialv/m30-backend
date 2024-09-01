package com.vinialv.m30.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.vinialv.m30.entities.ProjectCategory;
import com.vinialv.m30.repositories.ProjectCategoryRepository;
import com.vinialv.m30.exceptions.ProjectCategoryNotFoundException;
import com.vinialv.m30.exceptions.ProjectCategoryNonActiveFieldsException;

@Service
public class ProjectCategoryService {

  private final ProjectCategoryRepository repository;

  public ProjectCategoryService(ProjectCategoryRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<ProjectCategory> findAll() {
    return repository.findAll();
  }
  
  @GetMapping("/{id}")
  public ProjectCategory findById(@PathVariable("id") Long id) {
    return repository.findById(id).orElseThrow(ProjectCategoryNotFoundException::new);
  }
  
  @PostMapping
  public void save(@RequestBody ProjectCategory projectCategory) {
    if (projectCategory.getStatus() == null) {
      projectCategory.setStatus("A");
    }
    if (projectCategory.getDescription() == null) {
      throw new ProjectCategoryNonActiveFieldsException();
    } 
    repository.save(projectCategory);
  }

  @PutMapping("/{id}")
  public void update(@PathVariable("id") Long id, @RequestBody ProjectCategory projectCategory) {
    ProjectCategory category = repository.findById(id).orElseThrow(ProjectCategoryNotFoundException::new);

    if (projectCategory.getDescription() == null || projectCategory.getStatus() == null) {
      throw new ProjectCategoryNonActiveFieldsException();

    }
    repository.save(category);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    ProjectCategory category = repository.findById(id).orElseThrow(ProjectCategoryNotFoundException::new);
    repository.delete(category);
  }

}
