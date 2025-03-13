package com.vinialv.m30.controllers;

import java.util.List;
import java.util.Optional;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URLDecoder;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinialv.m30.entities.ProjectImage;
import com.vinialv.m30.services.ProjectImageService;
import com.vinialv.m30.config.FileStorageProperties;

import lombok.RequiredArgsConstructor;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/project-image")
public class ProjectImageController {

  private final ProjectImageService service;
  private final FileStorageProperties fileStorageProperties;

  private Path fileStorageLocation;  

  @PostConstruct
  private void initFileStorageLocation() {
    this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
        .toAbsolutePath().normalize();
  }

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

  @PostMapping("/upload/{id}")
  public void save(@PathVariable("id") Long projectId,
                   @RequestParam("title") String title,
                   @RequestParam("details") String details,
                   @RequestParam(value = "visibility", defaultValue = "T") String visibility,
                   @RequestParam("file") List<MultipartFile> files) {
    service.createImages(projectId, title, details, visibility, files);
  }

  @GetMapping("/{projectName:.+}/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String projectName, @PathVariable String fileName, HttpServletRequest request) throws IOException {
    String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
    Path filePath = fileStorageLocation.resolve(decodedFileName).normalize();
    try {
      Resource resource = new UrlResource(filePath.toUri());

      String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
      if (contentType == null) {
        contentType = "application/octet-stream";
      }
      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .body(resource);
    } catch (MalformedURLException ex) {
      return ResponseEntity.badRequest().body(null);
    }
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
