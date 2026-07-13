package com.hei.school.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Image(UUID id, String filename, String email, Instant createdAt) {}
