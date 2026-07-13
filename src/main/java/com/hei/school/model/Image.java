package com.hei.school.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Image(UUID id, String filename,String email, Instant createdAt) {}
