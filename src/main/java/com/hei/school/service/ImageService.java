package com.hei.school.service;

import com.hei.school.enums.ImageStatusEnum;
import com.hei.school.model.Image;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  Image uploadImage(MultipartFile file, String email);

  List<Image> getAllImages();

  Image getImageById(UUID id);

  void updateImageStatus(UUID id, ImageStatusEnum status);

  void updateImageProcessedUrl(UUID id, String processedUrl, ImageStatusEnum status);

  String getOriginalFilename(UUID id);
}
