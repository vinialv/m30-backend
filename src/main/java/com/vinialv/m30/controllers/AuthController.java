package com.vinialv.m30.controllers;

import com.vinialv.m30.entities.User;
import com.vinialv.m30.dto.LoginRequestDTO;
import com.vinialv.m30.dto.RefreshTokenRequestDTO;
import com.vinialv.m30.dto.RegisterRequestDTO;
import com.vinialv.m30.dto.ResponseDTO;
import com.vinialv.m30.infra.security.TokenService;
import com.vinialv.m30.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO body){
    User user = this.repository.findByEmail(body.email())
        .orElseThrow(() -> new RuntimeException("User not found"));
  
    if(passwordEncoder.matches(body.password(), user.getPassword())) {
      String accessToken = tokenService.generateAccessToken(user);
      String refreshToken = tokenService.generateRefreshToken(user);
      return ResponseEntity.ok(new ResponseDTO(user.getName(), accessToken, refreshToken));
    }
    
    return ResponseEntity.badRequest().build();
  }


  @PostMapping("/register")
  public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequestDTO body){
    Optional<User> existingUser = this.repository.findByEmail(body.email());
  
    if (existingUser.isPresent()) {
      return ResponseEntity.badRequest().build(); // já existe
    }
  
    User newUser = new User();
    newUser.setPassword(passwordEncoder.encode(body.password()));
    newUser.setEmail(body.email());
    newUser.setName(body.name());
    this.repository.save(newUser);

    String accessToken = tokenService.generateAccessToken(newUser);
    String refreshToken = tokenService.generateRefreshToken(newUser);

    return ResponseEntity.ok(new ResponseDTO(newUser.getName(), accessToken, refreshToken));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO body) {
    try {
      String subject = tokenService.validateToken(body.token());
      if (subject == null) return ResponseEntity.status(401).build();

      User user = repository.findByEmail(subject)
              .orElseThrow(() -> new RuntimeException("User not found"));

      String newAccessToken = tokenService.generateAccessToken(user);
      return ResponseEntity.ok(new ResponseDTO(user.getName(), newAccessToken, body.token()));
    } catch (Exception e) {
      return ResponseEntity.status(401).build();
    }
  }
}
