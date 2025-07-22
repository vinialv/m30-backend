package com.vinialv.m30.dto;

import java.util.List;

public record ReorderRequest(Long projectId, List<DisplayOrderUpdate> images) {}
