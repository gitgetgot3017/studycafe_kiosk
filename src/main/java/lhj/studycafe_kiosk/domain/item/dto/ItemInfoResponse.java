package lhj.studycafe_kiosk.domain.item.dto;

import lhj.studycafe_kiosk.domain.item.domain.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemInfoResponse {

    private ItemType itemType;
    private String itemName;
    private int price;
}
