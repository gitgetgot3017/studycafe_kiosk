package lhj.studycafe_kiosk.item;

import lhj.studycafe_kiosk.domain.Item;
import lhj.studycafe_kiosk.domain.ItemType;
import lhj.studycafe_kiosk.domain.ItemsPerItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public void registerItem(Item item) {

        itemRepository.saveItem(item);
    }

    @Transactional(readOnly = true)
    public boolean existItemName(String itemName) {

        return itemRepository.getExistItemName(itemName).size() > 0;
    }

    public void changeItemInfo(Long itemId, Item changedItem) {

        itemRepository.updateItemInfo(itemId, changedItem);
    }

    public List<ItemsPerItemType> getItemCategory() {

        List<ItemsPerItemType> itemsPerItemTypes = new ArrayList<>();
        for (Item item : itemRepository.getItemType()) {
            ItemType itemType = item.getItemType();
            List<Item> itemsPerItemType = itemRepository.getItems(itemType);
            itemsPerItemTypes.add(new ItemsPerItemType(itemType, itemsPerItemType));
        }
        return itemsPerItemTypes;
    }
}
