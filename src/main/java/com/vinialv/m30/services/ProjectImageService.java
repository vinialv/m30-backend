package com.vinialv.m30.services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vinialv.m30.config.FileStorageProperties;
import com.vinialv.m30.entities.Project;
import com.vinialv.m30.entities.ProjectImage;
import com.vinialv.m30.exceptions.NotFoundException;
import com.vinialv.m30.repositories.ProjectImageRepository;
import com.vinialv.m30.repositories.ProjectRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectImageService {

  private final ProjectImageRepository repository;
  private final ProjectRepository projectRepository;
  private final FileStorageProperties fileStorageProperties;
  
  private Path fileStorageLocation;  
  @PostConstruct
  private void initFileStorageLocation() {
    this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
        .toAbsolutePath().normalize();
  }

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

  public void createImages(Long projectId, String title, String details, String visibility, List<MultipartFile> files) {
    List<String> allowedTypes = Arrays.asList("image/webp", "image/avif", "image/png", "image/jpeg", "image/svg+xml");
    
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new NotFoundException("Projeto não encontrado!"));

    for (MultipartFile file : files) {
      if (!allowedTypes.contains(file.getContentType())) {
        throw new IllegalArgumentException("Tipo de imagem não permitido: " + file.getOriginalFilename());
      }
        
      try {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = fileStorageLocation.resolve(fileName);
        file.transferTo(targetLocation);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/v1/project-image/")
            .path(project.getName())
            .path("/")
            .path(fileName)
            .toUriString();

        ProjectImage projectImage = new ProjectImage();
        projectImage.setTitle(title);
        projectImage.setDetails(details);
        projectImage.setVisibility(visibility);
        projectImage.setUrl(fileDownloadUri);
        projectImage.setProject(project);

        repository.save(projectImage);
      } catch (Exception e) {
        throw new RuntimeException("Erro ao salvar arquivo: " + e.getMessage());
      }
    }
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
