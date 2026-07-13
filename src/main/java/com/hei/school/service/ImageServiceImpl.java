package com.hei.school.service;

import com.hei.school.endpoint.event.EventProducer;
import com.hei.school.endpoint.event.model.ImageSubmitted;
import com.hei.school.enums.ImageStatusEnum;
import com.hei.school.model.Image;
import com.hei.school.repository.JImageRepository;
import com.hei.school.repository.model.JImage;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final JImageRepository repository;
  private final S3Service s3Service;
  private final EventProducer<ImageSubmitted> eventProducer;

  @Override
  public Image uploadImage(MultipartFile file, String email) {
    String contentType = file.getContentType();
    if (contentType == null
        || (!contentType.equalsIgnoreCase("image/jpeg")
            && !contentType.equalsIgnoreCase("image/png"))) {
      throw new IllegalArgumentException("Only JPEG and PNG images are allowed");
    }

    UUID id = UUID.randomUUID();
    JImage jImage = new JImage();
    jImage.setId(id);
    jImage.setFilename(file.getOriginalFilename());
    jImage.setEmail(email);
    jImage.setCreatedAt(Instant.now());
    jImage.setStatus(ImageStatusEnum.PENDING);
    repository.save(jImage);

    String s3Key = id + "/original_" + file.getOriginalFilename();
    s3Service.upload(s3Key, file);

    ImageSubmitted event =
        ImageSubmitted.builder()
            .imageId(UUID.fromString(id.toString()))
            .s3Key(s3Key)
            .emailTo(email)
            .s3Bucket((String) s3Service.getBucketName())
            .build();
    eventProducer.accept(List.of(event));

    return toModel(jImage);
  }

  @Override
  public List<Image> getAllImages() {
    return repository.findAllByOrderByCreatedAtDesc().stream()
        .map(this::toModel)
        .collect(Collectors.toList());
  }

  @Override
  public Image getImageById(UUID id) {
    return repository
        .findById(id)
        .map(this::toModel)
        .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
  }

  @Override
  public void updateImageStatus(UUID id, ImageStatusEnum status) {
    repository
        .findById(id)
        .ifPresent(
            jImage -> {
              jImage.setStatus(status);
              repository.save(jImage);
            });
  }

  @Override
  public void updateImageProcessedUrl(UUID id, String processedUrl, ImageStatusEnum status) {
    repository
        .findById(id)
        .ifPresent(
            jImage -> {
              jImage.setProcessedUrl(processedUrl);
              jImage.setStatus(status);
              repository.save(jImage);
            });
  }

  @Override
  public String getOriginalFilename(UUID id) {
    return repository.findById(id).map(JImage::getFilename).orElse("Unknown");
  }

  private Image toModel(JImage jImage) {
    return Image.builder()
        .id(jImage.getId())
        .filename(jImage.getFilename())
        .email(jImage.getEmail())
        .createdAt(jImage.getCreatedAt())
        .processedUrl(jImage.getProcessedUrl())
        .status(jImage.getStatus())
        .build();
  }
}
