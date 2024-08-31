package com.vinialv.m30.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Table(name = "project")
@Check(constraints = "visibility IN ('A', 'I', 'T')")
@Data
@NoArgsConstructor
public class Project {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(unique = true)
  private String name;
  
  private String details;
  
  @Column(nullable = false)
  private LocalDateTime creationDate;
  
  private BigDecimal plantSize;
  
  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private ProjectCategory projectCategory;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;
  
  @ManyToOne
  @JoinColumn(name = "city_id", nullable = false)
  private City city;
  
  @ManyToOne
  @JoinColumn(name = "state_id", nullable = false)
  private State state;
  
  @Column(nullable = false, columnDefinition = "char(1) default 'T'")
  private String visibility;
  
}
