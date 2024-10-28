package lhj.studycafe_kiosk.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SubscriptionChangeRequest {

    @NotNull
    private Long beforeSubscriptionId;

    @NotNull
    private Long afterSubscriptionId;
}
