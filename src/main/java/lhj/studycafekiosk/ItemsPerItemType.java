package lhj.studycafekiosk;

import lhj.studycafekiosk.domain.item.domain.Item;
import lhj.studycafekiosk.domain.item.domain.ItemType;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemsPerItemType {

    private ItemType itemType;
    private List<Item> items;

    public ItemsPerItemType(ItemType itemType, List<Item> items) {
        this.itemType = itemType;
        this.items = items;
    }
}
