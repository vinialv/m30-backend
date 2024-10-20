package com.vinialv.m30.dto;

import java.util.List;

public record PaginatedResponseDTO<T>(
  List<T> data,
  PaginatedLinksDTO links,
  PaginatedMetaDTO meta
) {
  
}
