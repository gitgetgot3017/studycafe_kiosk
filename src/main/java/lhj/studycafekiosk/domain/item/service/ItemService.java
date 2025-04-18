package lhj.studycafekiosk.domain.item.service;

import lhj.studycafekiosk.domain.item.repository.ItemRepository;
import lhj.studycafekiosk.domain.item.domain.Item;
import lhj.studycafekiosk.domain.item.domain.ItemType;
import lhj.studycafekiosk.ItemsPerItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        for (ItemType itemType : ItemType.values()) {
            List<Item> itemsPerItemType = itemRepository.getItems(itemType);
            itemsPerItemTypes.add(new ItemsPerItemType(itemType, itemsPerItemType));
        }
        return itemsPerItemTypes;
    }

    public void removeItem(Long itemId) {

        Optional<Item> itemOp = itemRepository.getItem(itemId);
        if (itemOp.isPresent()) {
            itemRepository.deleteItem(itemOp.get());
        }
    }
}
