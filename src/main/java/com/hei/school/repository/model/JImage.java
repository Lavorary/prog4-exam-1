package com.hei.school.repository.model;

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
}
