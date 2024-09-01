package com.vinialv.m30.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vinialv.m30.entities.Project;
import com.vinialv.m30.repositories.ProjectRepository;

@Service
public class ProjectService {

  private final ProjectRepository repository;

  public ProjectService(ProjectRepository repository) {
    this.repository = repository;
  }

    public List<Project> findAll() {
    return repository.findAll();
  }

  public Optional<Project> findById(Long id) {
    return repository.findById(id);
  }

  public Optional<Project> findByName(String name) {
    return repository.findByName(name);
  }

  public void createProject(Project project) {
    validateProject(project);
    if (repository.findByName(project.getName()).isPresent()) {
      throw new IllegalArgumentException("Este nome de projeto já foi cadastrado!");
    }
    repository.save(project);
  }

  public void updateProject(Long id, Project project) {
    Project selectedProject = repository.findById(id).orElseThrow(null);
    
    if (project.getId() != null && !project.getId().equals(id)) {
      throw new IllegalArgumentException("O campo 'ID' não pode ser atualizado!");
    }
    if (project.getName() != null && !project.getName().equals(selectedProject.getName())) {
      if (repository.findByName(project.getName()).isPresent()) {
        throw new IllegalArgumentException("Este nome de projeto já foi cadastrado!");
      }
      selectedProject.setName(project.getName());
    }
    updateProjectFields(selectedProject, project);
    repository.save(selectedProject);
  }

  public void deleteProject(Long id) {
    repository.deleteById(id);
  }

  private void validateProject(Project project) {
    if (project.getName() == null) {
      throw new IllegalArgumentException("O campo 'Nome' deve ser informado!");
    }
    if (project.getCreationDate() == null) {
      throw new IllegalArgumentException("O campo 'Data de criação' deve ser informado!");
    }
    if (project.getPlantSize() == null) {
      throw new IllegalArgumentException("O campo 'Tamanho do projeto' deve ser informado!");
    }
    if (project.getCustomer() == null) {
      throw new IllegalArgumentException("O campo 'Cliente' deve ser informado!");
    }
    if (project.getCity() == null) {
      throw new IllegalArgumentException("O campo 'Cidade' deve ser informado!");
    }
    if (project.getVisibility() == null) {
      throw new IllegalArgumentException("O campo 'Visibilidade' deve ser informado!");
    }
  }

  private void updateProjectFields(Project existingProject, Project project) {
    if (project.getName() != null) {
      existingProject.setName(project.getName());
    }
    if (project.getCreationDate() != null) {
      existingProject.setCreationDate(project.getCreationDate());
    }
    if (project.getPlantSize() != null) {
      existingProject.setPlantSize(project.getPlantSize());
    }
    if (project.getVisibility() != null) {
      existingProject.setVisibility(project.getVisibility());
    }
  }
}
