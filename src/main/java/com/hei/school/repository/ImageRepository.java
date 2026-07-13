package com.hei.school.repository;

import com.hei.school.repository.model.JImage;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<JImage, UUID> {}
