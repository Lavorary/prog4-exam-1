package com.hei.school.service.event;

import com.hei.school.endpoint.event.model.ImageSubmitted;
import com.hei.school.enums.ImageStatusEnum;
import com.hei.school.mail.Email;
import com.hei.school.mail.Mailer;
import com.hei.school.service.ImageService;
import com.hei.school.service.S3Service;
import jakarta.mail.internet.InternetAddress;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageSubmittedService implements Consumer<ImageSubmitted> {
  private final ImageService imageService;
  private final S3Service s3Service;
  private final Mailer mailer;

  @Override
  @SneakyThrows
  public void accept(ImageSubmitted event) {
    UUID imageId = event.getImageId();
    try {
      imageService.updateImageStatus(imageId, ImageStatusEnum.PROCESSING);

      byte[] originalBytes = s3Service.download(event.getS3Bucket(), event.getS3Key());

      BufferedImage original = ImageIO.read(new java.io.ByteArrayInputStream(originalBytes));
      BufferedImage bwImage =
          new BufferedImage(
              original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
      bwImage.getGraphics().drawImage(original, 0, 0, null);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(bwImage, "jpg", baos);
      String bwKey = "bw/" + imageId + ".jpg";
      s3Service.upload(event.getS3Bucket(), bwKey, baos.toByteArray(), "image/jpeg");

      String bwUrl = String.format("https://%s.s3.amazonaws.com/%s", event.getS3Bucket(), bwKey);
      imageService.updateImageProcessedUrl(imageId, bwUrl, ImageStatusEnum.COMPLETED);

      String filename = imageService.getOriginalFilename(imageId);
      InternetAddress recipientAddress = new InternetAddress(event.getEmailTo());
      String emailBody =
          String.format(
              "Your image '%s' has been processed to black and white.\n\n"
                  + "View it here: %s\n\n"
                  + "Thank you for using our service!",
              filename, bwUrl);

      mailer.accept(
          new Email(
              recipientAddress,
              List.of(),
              List.of(),
              "Your Black & White Image is Ready",
              emailBody,
              List.of()));

    } catch (Exception e) {
      imageService.updateImageStatus(imageId, ImageStatusEnum.FAILED);
      throw e;
    }
  }
}
