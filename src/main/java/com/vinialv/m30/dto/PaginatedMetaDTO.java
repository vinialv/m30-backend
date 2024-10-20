package com.vinialv.m30.dto;

import java.util.List;

public record PaginatedMetaDTO(
  int currentPage,
  int from,
  int lastPage,
  List<PaginatedLinkDTO> links,
  String path,
  int perPage,
  int to,
  long total
) {
  
}
