package com.vinialv.m30.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vinialv.m30.entities.ProjectImage;
import com.vinialv.m30.exceptions.NotFoundException;
import com.vinialv.m30.repositories.ProjectImageRepository;
import com.vinialv.m30.repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectImageService {

  private final ProjectImageRepository repository;
  private final ProjectRepository projectRepository;

  public List<ProjectImage> findAll() {
    return repository.findAll();
  }

  public Optional<ProjectImage> findById(Long id) {
    return repository.findById(id);
  }

  public List<ProjectImage> findByProjectId(Long projectId) {
    if (projectRepository.findById(projectId).isEmpty()) {
      throw new IllegalArgumentException("Projeto não encontrado!");
    }
    return repository.findByProjectId(projectId);
  }
  
  public void createImage(ProjectImage projectImage) {
    validateProjectImage(projectImage);
    repository.save(projectImage);
  }

  public void updateImage(Long id, ProjectImage projectImage) {
    ProjectImage image = repository.findById(id).orElseThrow(() -> new NotFoundException("ID informado não encontrado."));
    
    if (projectImage.getId() != null && !projectImage.getId().equals(id)) {
      throw new IllegalArgumentException("O campo 'ID' não pode ser atualizado!");
    }

    updateProjectImageFields(image, projectImage);
    repository.save(image);
  }

  public void deleteImage(Long id) {
    if (!repository.existsById(id)) {
      throw new NotFoundException("Não existe imagem com o ID informado!");
    }
    repository.deleteById(id);
  }

  private void validateProjectImage(ProjectImage projectImage) {
    if (projectImage.getTitle() == null) {
      throw new IllegalArgumentException("O campo 'Título' deve ser informado!");
    }
    if (projectImage.getDetails() == null) {
      throw new IllegalArgumentException("O campo 'Detalhes' deve ser informado!");
    }
    if (projectImage.getUrl() == null) {
      throw new IllegalArgumentException("O campo 'URL' deve ser informado!");
    }
    if (projectImage.getVisibility() == null) {
      projectImage.setVisibility("T");
    }
    if (!isValidUrl(projectImage.getUrl())) {
      throw new IllegalArgumentException("URL inválida!");
    }
    if (projectRepository.findById(projectImage.getProject().getId()).isEmpty()) {
      throw new IllegalArgumentException("Projeto não encontrado!");
    }
  }

  private boolean isValidUrl(String url) {
    String urlRegex = "^(?:https?:\\/\\/)?(w{3}\\.)?[\\w_-]+((\\.\\w{2,}){1,2})(\\/([\\w\\._-]+\\/?)*(\\?[\\w_-]+=[^\\?\\/&]*(\\&[\\w_-]+=[^\\?\\/&]*)*)?)?$";
    return url.matches(urlRegex);
  }

  private void updateProjectImageFields(ProjectImage existingImage, ProjectImage image) {
    if (image.getTitle() != null) {
      existingImage.setTitle(image.getTitle());
    }
    if (image.getDetails() != null) {
      existingImage.setDetails(image.getDetails());
    }
    if (image.getUrl() != null) {
      existingImage.setUrl(image.getUrl());
    }
    if (image.getVisibility() != null) {
      existingImage.setVisibility(image.getVisibility());
    }
  }

}
