package com.hei.school.model;

import com.hei.school.enums.ImageStatusEnum;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Image(
    UUID id,
    String filename,
    String email,
    Instant createdAt,
    String processedUrl,
    ImageStatusEnum status) {}
