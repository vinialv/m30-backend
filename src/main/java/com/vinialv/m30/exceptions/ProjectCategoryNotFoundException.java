package com.vinialv.m30.exceptions;

public class ProjectCategoryNotFoundException extends RuntimeException {

  public ProjectCategoryNotFoundException() {
    super("Nenhuma categoria foi encontrada com o ID informado.");
  }

  public ProjectCategoryNotFoundException(String message) {
    super(message);
  }

}
