package com.vinialv.m30.infra;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class RestErrorMessage {

  private HttpStatus status;
  private String message;

} 