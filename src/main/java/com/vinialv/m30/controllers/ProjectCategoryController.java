package com.vinialv.m30.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vinialv.m30.dto.PaginatedLinkDTO;
import com.vinialv.m30.dto.PaginatedMetaDTO;
import com.vinialv.m30.dto.PaginatedLinksDTO;
import com.vinialv.m30.dto.PaginatedResponseDTO;
import com.vinialv.m30.entities.ProjectCategory;
import com.vinialv.m30.services.ProjectCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/project-category")
public class ProjectCategoryController {

  private final ProjectCategoryService service;


  @GetMapping("/all")
  public List<ProjectCategory> findAll() {
    return service.findAll();
  }

  @GetMapping
  public ResponseEntity<PaginatedResponseDTO<ProjectCategory>> findAllPageable(@RequestParam(defaultValue = "1") int page, 
                                                                               @RequestParam(required = false) String search,  
                                                                               @RequestParam(required = false) String status,   
                                                                               @PageableDefault(size = 10) Pageable pageable) throws InterruptedException {
    Pageable adjustedPageable = PageRequest.of(Math.max(0, page - 1), pageable.getPageSize());
    Page<ProjectCategory> pageCategory = service.findAllPageable(adjustedPageable, status, search);
    String baseUrl = "http://localhost:8080/v1/project-category";
    String first = baseUrl + "?page=1&size=" + pageable.getPageSize();
    String last = baseUrl + "?page=" + pageCategory.getTotalPages() + "&size=" + pageable.getPageSize();
    String prev = pageCategory.hasPrevious() ? baseUrl + "?page=" + (pageCategory.getNumber()) + "&size=" + pageable.getPageSize() : null;
    String next = pageCategory.hasNext() ? baseUrl + "?page=" + (pageCategory.getNumber() + 2) + "&size=" + pageable.getPageSize() : null;

    PaginatedLinksDTO links = new PaginatedLinksDTO(first, last, prev, next);
    
    List<PaginatedLinkDTO> metaLinks = new ArrayList<>();
    metaLinks.add(new PaginatedLinkDTO(prev, "&laquo; Previous", pageCategory.hasPrevious()));
    
    int totalPages = pageCategory.getTotalPages();
    int currentPage = pageCategory.getNumber() + 1;    

    if (currentPage == 1 || currentPage == 2 || currentPage == 3 || currentPage == 4) { 
      // Exibir as primeiras 7 páginas
      for (int i = 1; i <= Math.min(7, totalPages); i++) {
        boolean isActive = (i == currentPage);
        metaLinks.add(new PaginatedLinkDTO(baseUrl + "?page=" + i + "&size=" + pageable.getPageSize(), String.valueOf(i), isActive));
      }
      // Adicionar "..."
      if (totalPages > 7) {
        metaLinks.add(new PaginatedLinkDTO(null, "...", false));
      }
      // Adicionar as últimas 3 páginas
      for (int i = Math.max(totalPages - 2, 8); i <= totalPages; i++) {
        boolean isActive = (i == currentPage);
        metaLinks.add(new PaginatedLinkDTO(baseUrl + "?page=" + i + "&size=" + pageable.getPageSize(), String.valueOf(i), isActive));
      }
    } else if (currentPage >= totalPages - 2 || currentPage >= totalPages - 3 || currentPage >= totalPages - 4) {
      // Exibir as primeiras 3 páginas
      for (int i = 1; i <= Math.min(3, totalPages); i++) {
        boolean isActive = (i == currentPage);
        metaLinks.add(new PaginatedLinkDTO(baseUrl + "?page=" + i + "&size=" + pageable.getPageSize(), String.valueOf(i), isActive));
      }
      // Adicionar "..."
      if (totalPages > 6) {
        metaLinks.add(new PaginatedLinkDTO(null, "...", false));
      }
      // Adicionar as páginas próximas à antepenúltima
      for (int i = Math.max(totalPages - 6, 4); i <= totalPages; i++) {
        boolean isActive = (i == currentPage);
        metaLinks.add(new PaginatedLinkDTO(baseUrl + "?page=" + i + "&size=" + pageable.getPageSize(), String.valueOf(i), isActive));
      }
    } else {
      // Exibir as primeiras 3 páginas
      for (int i = 1; i <= 3; i++) {
        boolean isActive = (i == currentPage);
        metaLinks.add(new PaginatedLinkDTO(baseUrl + "?page=" + i + "&size=" + pageable.getPageSize(), String.valueOf(i), isActive));
      }
      // Adicionar "..."
      metaLinks.add(new PaginatedLinkDTO(null, "...", false));
      // Adicionar as páginas próximas à página atual
      for (int i = Math.max(currentPage - 2, 4); i <= Math.min(currentPage + 2, totalPages - 3); i++) {
        boolean isActive = (i == currentPage);
        metaLinks.add(new PaginatedLinkDTO(baseUrl + "?page=" + i + "&size=" + pageable.getPageSize(), String.valueOf(i), isActive));
      }
      // Adicionar "..."
      metaLinks.add(new PaginatedLinkDTO(null, "...", false));
      // Adicionar as últimas 3 páginas
      for (int i = Math.max(totalPages - 2, currentPage + 3); i <= totalPages; i++) {
        boolean isActive = (i == currentPage);
        metaLinks.add(new PaginatedLinkDTO(baseUrl + "?page=" + i + "&size=" + pageable.getPageSize(), String.valueOf(i), isActive));
      }
    }
    metaLinks.add(new PaginatedLinkDTO(next, "Next &raquo;", pageCategory.hasNext()));
    
    PaginatedMetaDTO meta = new PaginatedMetaDTO(
      pageCategory.getNumber() + 1, 
      pageCategory.getNumber() * pageable.getPageSize() + 1, 
      pageCategory.getTotalPages(), 
      metaLinks,
      baseUrl,
      pageable.getPageSize(), 
      pageCategory.getNumberOfElements() + (pageCategory.getNumber() * pageable.getPageSize()), 
      pageCategory.getTotalElements()
    );

    PaginatedResponseDTO<ProjectCategory> response = new PaginatedResponseDTO<>(pageCategory.getContent(), links, meta);
    
    //Thread.sleep(3000);

    return new ResponseEntity<>(response, HttpStatus.OK);
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
