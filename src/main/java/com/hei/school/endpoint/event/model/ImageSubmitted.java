package com.hei.school.endpoint.event.model;

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
  private String bucketKey;
  private String imageName;
  private String emailTo;

  @Override
  public Duration maxConsumerDuration() {
    return Duration.ofSeconds(60);
  }

  @Override
  public Duration maxConsumerBackoffBetweenRetries() {
    return Duration.ofSeconds(30);
  }
}
