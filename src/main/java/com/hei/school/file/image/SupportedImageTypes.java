package com.hei.school.file.image;

import java.util.Map;
import java.util.Set;
import org.springframework.http.MediaType;

public class SupportedImageTypes {
  public static final Set<String> SUPPORTED_CONTENT_TYPES =
      Set.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);

  private static final Map<String, String> CONTENT_TYPE_TO_EXTENSION =
      Map.of(
          MediaType.IMAGE_JPEG_VALUE, "jpg",
          MediaType.IMAGE_PNG_VALUE, "png");

  private SupportedImageTypes() {}

  public static boolean isSupported(String contentType) {
    return SUPPORTED_CONTENT_TYPES.contains(contentType);
  }

  public static String extensionOf(String contentType) {
    var extension = CONTENT_TYPE_TO_EXTENSION.get(contentType);
    if (extension == null) {
      throw new IllegalArgumentException("Unsupported content type: " + contentType);
    }
    return extension;
  }
}
