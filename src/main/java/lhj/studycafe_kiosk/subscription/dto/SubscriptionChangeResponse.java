package lhj.studycafe_kiosk.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscriptionChangeResponse {

    private String domain;
    private String message;
}
