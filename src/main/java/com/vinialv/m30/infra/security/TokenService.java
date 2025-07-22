package com.vinialv.m30.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vinialv.m30.dto.ResponseDTO;
import com.vinialv.m30.entities.User;
import com.vinialv.m30.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {
  private final UserRepository repository;
  private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
  private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
  private static final String ISSUER = "m30-auth-api";

  @Value("${api.security.token.secret}")
  private String secret;

  public String generateAccessToken(User user) {
    Algorithm algorithm = getSignAlgorithm();

    return JWT.create()
      .withIssuer(ISSUER)
      .withSubject(user.getEmail())
      .withExpiresAt(this.generateExpirationDate(ACCESS_TOKEN))
      .sign(algorithm);
  }
      

  public String generateRefreshToken(User user) {
    Algorithm algorithm = getSignAlgorithm();

    return JWT.create()
      .withIssuer(ISSUER)
      .withSubject(user.getEmail())
      .withExpiresAt(this.generateExpirationDate(REFRESH_TOKEN))
      .sign(algorithm);
    }

  public String validateToken(String token){
    try {
      Algorithm algorithm = getSignAlgorithm();
      return JWT.require(algorithm)
        .withIssuer(ISSUER)
        .build()
        .verify(token)
        .getSubject();
    } catch (JWTVerificationException exception) {
      return null;
    }
  }
    
public ResponseDTO refreshTokens(String refreshToken) {
  String subject = validateToken(refreshToken);
  if (subject == null) throw new RuntimeException("Invalid refresh token");

  User user = repository.findByEmail(subject)
    .orElseThrow(() -> new RuntimeException("User not found"));

  String newAccessToken = generateAccessToken(user);
  String newRefreshToken = generateRefreshToken(user);

  return new ResponseDTO(user.getName(), newAccessToken, newRefreshToken);
}

  private Date generateExpirationDate(String tokenType) {
    if (ACCESS_TOKEN.equals(tokenType)) {
      return Date.from(Instant.now().plusSeconds(60 * 60 * 24 * 7));
    }
    return Date.from(Instant.now().plusSeconds(60 * 60 * 24 * 7));
  }

  private Algorithm getSignAlgorithm() {
      return Algorithm.HMAC256(secret);
  }
}