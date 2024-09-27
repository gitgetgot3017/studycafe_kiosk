package lhj.studycafe_kiosk.item.dto;

import lhj.studycafe_kiosk.domain.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemInfoResponse {

    private ItemType itemType;
    private String itemName;
    private int price;
}
