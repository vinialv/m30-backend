package com.vinialv.m30.exceptions;

public class ProjectCategoryNonActiveFieldsException extends RuntimeException {

  public ProjectCategoryNonActiveFieldsException() {
    super("Não foram informados todos os campos obrigatórios.");
  }

  public ProjectCategoryNonActiveFieldsException(String message) {
    super(message);
  }

}
