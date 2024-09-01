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
@Table(name = "customer")
@Check(constraints = "status IN ('A', 'I')")
@Data
@NoArgsConstructor
public class Customer {
    
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private Long phone;

  @ManyToOne
  @JoinColumn(name = "city_id", nullable = false)
  private City city;

  @Column(nullable = false, columnDefinition = "char(1) default 'A'")
  private String status;

}
