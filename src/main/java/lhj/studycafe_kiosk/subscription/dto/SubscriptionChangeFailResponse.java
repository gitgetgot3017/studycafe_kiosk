package lhj.studycafe_kiosk.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscriptionChangeFailResponse {

    private String domain;
    private String message;
}
