package com.hei.school.repository.model;

import com.hei.school.enums.ImageStatusEnum;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image")
public class JImage {
  @Id private UUID id;

  @Column(nullable = false)
  private String filename;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private Instant createdAt;

  @Column private String processedUrl;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ImageStatusEnum status;
}
