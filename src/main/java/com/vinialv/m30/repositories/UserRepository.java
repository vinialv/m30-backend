package com.vinialv.m30.repositories;

import java.util.Optional;

import com.vinialv.m30.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
