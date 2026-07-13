package com.hei.school.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class S3Service {
  private final Path localStorage = Paths.get("local-storage");

  public S3Service() {
    try {
      Files.createDirectories(localStorage);
      log.info("Local storage initialized at: {}", localStorage.toAbsolutePath());
    } catch (Exception e) {
      log.warn("Could not create local storage directory", e);
    }
  }

  @SneakyThrows
  public void upload(String key, MultipartFile file) {
    Path filePath = localStorage.resolve(key);
    Files.createDirectories(filePath.getParent());
    file.transferTo(filePath.toFile());
    log.info("File saved locally: {}", filePath);
  }

  @SneakyThrows
  public void upload(String bucket, String key, byte[] content, String contentType) {
    Path filePath = localStorage.resolve(key);
    Files.createDirectories(filePath.getParent());
    Files.write(filePath, content);
    log.info("File saved locally: {}", filePath);
  }

  @SneakyThrows
  public byte[] download(String bucket, String key) {
    Path filePath = localStorage.resolve(key);
    return Files.readAllBytes(filePath);
  }

  public String getBucketName() {
    return "local-bucket";
  }
}
