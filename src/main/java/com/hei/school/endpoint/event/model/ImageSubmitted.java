package com.hei.school.endpoint.event.model;

import com.hei.school.enums.ImageStatusEnum;
import java.time.Duration;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ImageSubmitted extends PojaEvent {
  private String imageId;
  private String imageName;
  private String emailTo;
  private String format;
  private ImageStatusEnum status;

  @Override
  public Duration maxConsumerDuration() {
    return Duration.ofSeconds(60);
  }

  @Override
  public Duration maxConsumerBackoffBetweenRetries() {
    return Duration.ofSeconds(30);
  }
}
