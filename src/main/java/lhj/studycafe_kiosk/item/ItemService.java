package lhj.studycafe_kiosk.item;

import lhj.studycafe_kiosk.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
