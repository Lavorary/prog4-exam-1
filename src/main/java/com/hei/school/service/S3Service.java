package com.hei.school.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@AllArgsConstructor
public class S3Service {
  private final S3Client s3Client;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  @SneakyThrows
  public void upload(String key, MultipartFile file) {
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(file.getContentType())
            .build();

    s3Client.putObject(
        putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
  }

  @SneakyThrows
  public void upload(String bucket, String key, byte[] content, String contentType) {
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder().bucket(bucket).key(key).contentType(contentType).build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
  }

  @SneakyThrows
  public byte[] download(String key) {
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder().bucket(bucketName).key(key).build();

    return s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
  }

  @SneakyThrows
  public byte[] download(String bucket, String key) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key).build();

    return s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
  }

  public Object getBucketName() {
    return bucketName;
  }
}
