package lhj.studycafekiosk.domain.item.dto;

import lhj.studycafekiosk.domain.item.domain.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemInfoResponse {

    private ItemType itemType;
    private String itemName;
    private int price;
}
