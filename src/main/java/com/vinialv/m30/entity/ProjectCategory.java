package com.vinialv.m30.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Check;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "project_category")
@Check(constraints = "status IN ('A', 'I')")
@Data
@NoArgsConstructor
public class ProjectCategory {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false, unique = true)
  private String description;
  
  @Column(nullable = false, columnDefinition = "char(1) default 'A'")
  private String status;
  
}
