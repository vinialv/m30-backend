package com.vinialv.m30.infra;

import com.vinialv.m30.exceptions.ProjectCategoryNotFoundException;
import com.vinialv.m30.exceptions.ProjectCategoryNonActiveFieldsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ProjectCategoryNotFoundException.class)
  public ResponseEntity<RestErrorMessage> eventNotFoundHandler(ProjectCategoryNotFoundException exception) {
    RestErrorMessage response = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(ProjectCategoryNonActiveFieldsException.class)
  public ResponseEntity<RestErrorMessage> eventNotFoundHandler(ProjectCategoryNonActiveFieldsException exception) {
    RestErrorMessage response = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  private ResponseEntity<RestErrorMessage> runtimeErrorHandler(RuntimeException exception){
      RestErrorMessage response = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

}
