package lhj.studycafe_kiosk.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SubscriptionListResponse {

    private String itemName;

    private boolean isRepresentative;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Duration leftTime;
}
