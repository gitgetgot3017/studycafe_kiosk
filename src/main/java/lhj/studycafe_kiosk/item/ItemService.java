package lhj.studycafe_kiosk.item;

import lhj.studycafe_kiosk.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void registerItem(Item item) {

        itemRepository.saveItem(item);
    }

    public boolean existItemName(String itemName) {

        return itemRepository.getExistItemName(itemName).size() > 0;
    }
}
