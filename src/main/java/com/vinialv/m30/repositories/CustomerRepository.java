package com.vinialv.m30.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vinialv.m30.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByEmail(String email);
  Optional<Customer> findByName(String name);
  Page<Customer> findByStatusAndNameContaining(String status, String name, Pageable pageable);
  Page<Customer> findByStatus(String status, Pageable pageable);
  Page<Customer> findByNameContaining(String name, Pageable pageable);
  Page<Customer> findAll(Pageable pageable);
}