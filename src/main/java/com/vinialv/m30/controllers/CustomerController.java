package com.vinialv.m30.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinialv.m30.entities.Customer;
import com.vinialv.m30.services.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customer")
public class CustomerController {

  private final CustomerService service;

  @GetMapping
  public List<Customer> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Customer> findById(@PathVariable("id") Long id) {
    Optional<Customer> customer = service.findById(id);
    return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public void save(@RequestBody Customer customer) {
    service.createCustomer(customer);
  }

  @PutMapping("/{id}")
  public void update(@PathVariable("id") Long id, @RequestBody Customer customer) {
    service.updateCustomer(id, customer);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    service.deleteCustomer(id);
  }

}
