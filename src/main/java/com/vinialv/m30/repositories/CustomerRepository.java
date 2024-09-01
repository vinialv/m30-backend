package com.vinialv.m30.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vinialv.m30.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByEmail(String email);
}