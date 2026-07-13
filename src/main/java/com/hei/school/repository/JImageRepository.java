package com.hei.school.repository;

import com.hei.school.repository.model.JImage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JImageRepository extends JpaRepository<JImage, UUID> {
  List<JImage> findAllByOrderByCreatedAtDesc();
}
