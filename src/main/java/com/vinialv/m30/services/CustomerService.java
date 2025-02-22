package com.vinialv.m30.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vinialv.m30.entities.Customer;
import com.vinialv.m30.exceptions.NotFoundException;
import com.vinialv.m30.repositories.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository repository;
  private static final String ONLY_ACTIVE = "only-active";
  private static final String ONLY_INACTIVE = "only-inactive";

  public List<Customer> findAll() {
    return repository.findAll();
  }


  public Page<Customer> findAllPageable(Pageable pageable) {
    return repository.findAll(pageable);
  }
  
  public Page<Customer> findAllPageable(Pageable pageable, String status, String search) {
    if (ONLY_ACTIVE.equals(status)) {
      status = "A";
    } else if (ONLY_INACTIVE.equals(status)) {
      status = "I";
    }

    if (status != null && search != null) {
      return repository.findByStatusAndNameContaining(status, search, pageable);
    } else if (status != null) {
      return repository.findByStatus(status, pageable);
    } else if (search != null) {
      return repository.findByNameContaining(search, pageable);
    } else {
      return repository.findAll(pageable);
    }
  }

  public Optional<Customer> findById(Long id) {
    return repository.findById(id);
  }

  public Optional<Customer> findByName(String name) {
    return repository.findByName(name);
  }

  public Page<Customer> findByNameContaining(String name, Pageable pageable) {
    return repository.findByNameContaining(name, pageable);
  }

  public Optional<Customer> findByEmail(String email) {
    return repository.findByEmail(email);
  }

  public void createCustomer(Customer customer) {
    validateCustomer(customer);
    if (repository.findByEmail(customer.getEmail()).isPresent()) {
      throw new IllegalArgumentException("E-mail já cadastrado!");
    }
    customer.setStatus(Optional.ofNullable(customer.getStatus()).orElse("A"));
    repository.save(customer);
  }

  public void updateCustomer(Long id, Customer customer) {
    Customer existingCustomer = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Não existe cliente com o ID informado!"));

    if (customer.getId() != null && !customer.getId().equals(id)) {
      throw new IllegalArgumentException("O campo 'ID' não pode ser atualizado!");
    }

    if (customer.getEmail() != null && !customer.getEmail().equals(existingCustomer.getEmail())) {
      if (repository.findByEmail(customer.getEmail()).isPresent()) {
        throw new IllegalArgumentException("E-mail já cadastrado!");
      }
      existingCustomer.setEmail(customer.getEmail());
    }

    updateCustomerFields(existingCustomer, customer);
    repository.save(existingCustomer);
  }

  public void deleteCustomer(Long id) {
    if (!repository.existsById(id)) {
      throw new NotFoundException("Não existe cliente com o ID informado!");
    }
    repository.deleteById(id);
  }

  private void validateCustomer(Customer customer) {
    if (customer.getName() == null) {
      throw new IllegalArgumentException("O campo 'Nome' deve ser informado!");
    }
    if (customer.getEmail() == null) {
      throw new IllegalArgumentException("O campo 'E-mail' deve ser informado!");
    }
    if (customer.getPhone() == null) {
      throw new IllegalArgumentException("O campo 'Telefone' deve ser informado!");
    }
    if (customer.getCity() == null || customer.getCity().getId() == null) {
      throw new IllegalArgumentException("O campo 'Cidade' deve ser informado!");
    }
  }

  private void updateCustomerFields(Customer existingCustomer, Customer customer) {
    if (customer.getName() != null) {
      existingCustomer.setName(customer.getName());
    }
    if (customer.getPhone() != null) {
      existingCustomer.setPhone(customer.getPhone());
    }
    if (customer.getCity() != null) {
      existingCustomer.setCity(customer.getCity());
    }
    if (customer.getStatus() != null) {
      existingCustomer.setStatus(customer.getStatus());
    }
  }
}