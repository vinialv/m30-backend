package com.vinialv.m30.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vinialv.m30.entities.ProjectCategory;
import com.vinialv.m30.exceptions.NotFoundException;
import com.vinialv.m30.repositories.ProjectCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectCategoryService {

  private final ProjectCategoryRepository repository;
  private static final String ONLY_ACTIVE = "only-active";
  private static final String ONLY_INACTIVE = "only-inactive";

  public List<ProjectCategory> findAll() {
    return repository.findAll();
  }

  public Page<ProjectCategory> findAllPageable(Pageable pageable) {
    return repository.findAll(pageable);
  }
  
  public Page<ProjectCategory> findAllPageable(Pageable pageable, String status, String search) {
    if (ONLY_ACTIVE.equals(status)) {
      status = "A";
    } else if (ONLY_INACTIVE.equals(status)) {
      status = "I";
    }

    if (status != null && search != null) {
      return repository.findByStatusAndDescriptionContaining(status, search, pageable);
    } else if (status != null) {
      return repository.findByStatus(status, pageable);
    } else if (search != null) {
      return repository.findByDescriptionContaining(search, pageable);
    } else {
      return repository.findAll(pageable);
    }
  }

  public Optional<ProjectCategory> findById(Long id) {
    return repository.findById(id);
  }

  public Optional<ProjectCategory> findByDescription(String description) {
    return repository.findByDescription(description);
  }

  public Page<ProjectCategory> findByDescriptionContaining(String description, Pageable pageable) {
    return repository.findByDescriptionContaining(description, pageable);
  }
  
  public void createProjectCategory(ProjectCategory projectCategory) {
    validateProjectCategory(projectCategory);
    if (repository.findByDescription(projectCategory.getDescription()).isPresent()) {
      throw new IllegalArgumentException("Categoria já cadastrada!");
    }
    projectCategory.setStatus(Optional.ofNullable(projectCategory.getStatus()).orElse("A"));
    repository.save(projectCategory);
  }

  public void updateProjectCategory(Long id, ProjectCategory projectCategory) {
    ProjectCategory existingCategory = repository.findById(id).orElseThrow(() -> new NotFoundException("Categoria não encontrada.")); 
    if (projectCategory.getId() != null && !projectCategory.getId().equals(id)) {
      throw new IllegalArgumentException("O campo 'ID' não pode ser atualizado!");
    }
    if (projectCategory.getDescription() != null && !projectCategory.getDescription().equals(existingCategory.getDescription())) {
      if (repository.findByDescription(projectCategory.getDescription()).isPresent()) {
        throw new IllegalArgumentException("Categoria já cadastrada!");
      }
      existingCategory.setDescription(projectCategory.getDescription());
    }
    updateProjectCategory(existingCategory, projectCategory);
    repository.save(existingCategory);
  }

  public void deleteProjectCategory(Long id) {
    if (!repository.existsById(id)) {
      throw new NotFoundException("Não existe categoria com o ID informado!");
    }
    repository.deleteById(id);
  }

  private void validateProjectCategory(ProjectCategory projectCategory) {
    if (projectCategory.getDescription() == null) {
      throw new IllegalArgumentException("O campo 'Descrição' deve ser informado!");
    }
  }

  private void updateProjectCategory(ProjectCategory existingProjectCategory, ProjectCategory projectCategory) {
    if (projectCategory.getDescription() != null) {
      existingProjectCategory.setDescription(projectCategory.getDescription());
    }
    if (projectCategory.getStatus() != null) {
      existingProjectCategory.setStatus(projectCategory.getStatus());
    }
  }

}
