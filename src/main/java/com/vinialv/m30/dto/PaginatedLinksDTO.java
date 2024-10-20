package com.vinialv.m30.dto;

public record PaginatedLinksDTO(
  String first,
  String last,
  String prev,
  String next
) {}
