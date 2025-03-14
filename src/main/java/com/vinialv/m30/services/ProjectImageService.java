package com.vinialv.m30.services;

import java.io.IOException;
import java.nio.file.Files;
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

  public void createImages(Long projectId, String visibility, List<MultipartFile> files) {
    List<String> allowedTypes = Arrays.asList("image/webp", "image/avif", "image/png", "image/jpeg", "image/svg+xml");
    
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new NotFoundException("Projeto não encontrado!"));

    Path projectDir = fileStorageLocation.resolve("project-" + projectId);
    if (!Files.exists(projectDir)) {
      try {
        Files.createDirectories(projectDir);
      } catch (IOException e) {
        throw new RuntimeException("Erro ao criar a pasta do projeto: " + e.getMessage());
      }
    }

    for (MultipartFile file : files) {
      if (!allowedTypes.contains(file.getContentType())) {
        throw new IllegalArgumentException("Tipo de imagem não permitido: " + file.getOriginalFilename());
      }
        
      try {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = projectDir.resolve(fileName);
        file.transferTo(targetLocation);

       String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
         .path("/v1/project-image/")
         .path("/" + project.getId())
         .path("/")
         .path(fileName)
         .toUriString();

        ProjectImage projectImage = new ProjectImage();
        projectImage.setVisibility(visibility);
        projectImage.setPath(fileDownloadUri);
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
    ProjectImage projectImage = repository.findById(id).orElseThrow(() -> new NotFoundException("Imagem não encontrada!"));
    Long projectId = projectImage.getProject().getId();
    Path projectDir = fileStorageLocation.resolve("project-" + projectId);
    try {
      Path filePath = projectDir.resolve(Paths.get(projectImage.getPath()).getFileName());
      System.out.println("Path to file: " + filePath);
      Files.deleteIfExists(filePath);

      long remainingImages = repository.countByProjectId(id);
      if (remainingImages == 0) {
        try {
          Files.deleteIfExists(projectDir);
        } catch (IOException e) {
          throw new RuntimeException("Erro ao excluir a pasta do projeto: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao excluir o arquivo: " + e.getMessage());
    }
    repository.deleteById(id);
  }

  public void deleteImagesByProject(Long id) {
    List<ProjectImage> images = repository.findByProjectId(id);
    if (images.isEmpty()) {
      throw new NotFoundException("Nenhuma imagem encontrada para este projeto.");
    }
    Long projectId = images.get(0).getProject().getId();
    Path projectDir = fileStorageLocation.resolve("project-" + projectId);
    for (ProjectImage image : images) {
      try {
        Path filePath = projectDir.resolve(Paths.get(image.getPath()).getFileName());
        Files.deleteIfExists(filePath);
      } catch (IOException e) {
        throw new RuntimeException("Erro ao excluir o arquivo: " + e.getMessage());
      }
    }
    try {
      Files.deleteIfExists(projectDir);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao excluir a pasta do projeto: " + e.getMessage());
    }

    repository.deleteAll(images);
  }

  private void updateProjectImageFields(ProjectImage existingImage, ProjectImage image) {
    if (image.getVisibility() != null) {
      existingImage.setVisibility(image.getVisibility());
    }
  }

}
