package com.hei.school.endpoint.rest.controller.health;

import com.hei.school.model.Image;
import com.hei.school.service.ImageService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class ImageController {
  private final ImageService imageService;

  @PostMapping("/images")
  public String uploadImage(
      @RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
    Image image = imageService.uploadImage(file, email);
    return "Image uploaded successfully with ID: " + image.id();
  }

  @GetMapping("/images")
  public List<Image> getAllImages() {
    return imageService.getAllImages();
  }

  @GetMapping("/images/{id}")
  public Image getImageById(@PathVariable UUID id) {
    return imageService.getImageById(id);
  }
}
