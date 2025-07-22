package com.vinialv.m30.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Check;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "project_image")
@Check(constraints = "visibility IN ('A', 'I', 'T')")
@Data
@NoArgsConstructor
public class ProjectImage {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @Column(nullable = false)
  private String path;
  
  //@Column(nullable = false)
  private Long displayOrder;

  @Column(nullable = false, columnDefinition = "char(1) default 'T'")
  private String visibility;
  
}
