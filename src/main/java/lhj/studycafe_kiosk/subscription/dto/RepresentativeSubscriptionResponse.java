package lhj.studycafe_kiosk.subscription.dto;

import lhj.studycafe_kiosk.domain.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RepresentativeSubscriptionResponse {

    private String itemName;
    private String endDateTime;
    private ItemType itemType;
}
