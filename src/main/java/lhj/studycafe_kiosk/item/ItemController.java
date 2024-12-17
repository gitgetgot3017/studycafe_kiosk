package lhj.studycafe_kiosk.item;

import lhj.studycafe_kiosk.domain.Item;
import lhj.studycafe_kiosk.domain.ItemType;
import lhj.studycafe_kiosk.domain.ItemsPerItemType;
import lhj.studycafe_kiosk.item.dto.*;
import lhj.studycafe_kiosk.item.exception.DuplicateItemNameException;
import lhj.studycafe_kiosk.item.exception.NoItemException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @PostMapping
    public HttpEntity<ItemRegResponse> registerItem(@RequestBody @Validated ItemRegRequest itemRegRequest) {

        validateDuplicateItemName(itemRegRequest.getItemName());

        Item item = changeItemRegRequestToItem(itemRegRequest);
        itemService.registerItem(item);

        ItemRegResponse itemRegResponse = new ItemRegResponse("상품을 정상적으로 등록하였습니다.", item.getId());
        return new ResponseEntity<>(itemRegResponse, HttpStatus.CREATED);
    }

    @GetMapping("/detail")
    public HttpEntity<List<ItemInfoResponse>> findItems(@RequestParam("itemType") ItemType itemType) {

        List<Item> items = itemRepository.getItems(itemType);

        if (items.isEmpty()) {
            throw new NoItemException("아직 상품이 등록되지 않았습니다.");
        }

        List<ItemInfoResponse> responseItems = changeAllItemToItemInfoResponse(items);
        return new ResponseEntity<>(responseItems, HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public HttpEntity<ItemChangeResponse> changeItemInfo(@PathVariable("itemId") Long itemId, @RequestBody @Validated ItemChangeRequest itemChangeRequest) {

        validateDuplicateItemName(itemChangeRequest.getItemName());

        Item item = changeItemChangeRequestToItem(itemChangeRequest);
        itemService.changeItemInfo(itemId, item);

        ItemChangeResponse itemChangeResponse = new ItemChangeResponse("상품 수정이 정상적으로 완료되었습니다.", itemId);
        return new ResponseEntity<>(itemChangeResponse, HttpStatus.ACCEPTED);
    }

    private void validateDuplicateItemName(String itemName) {

        if (itemService.existItemName(itemName)) {
            throw new DuplicateItemNameException("이미 등록된 상품입니다.");
        }
    }

    private Item changeItemRegRequestToItem(ItemRegRequest itemRegRequest) {

        return new Item(itemRegRequest.getItemType(), itemRegRequest.getItemName(), itemRegRequest.getDuration(), itemRegRequest.getPrice());
    }

    private ItemInfoResponse changeItemToItemInfoResponse(Item item) {

        return new ItemInfoResponse(item.getItemType(), item.getItemName(), item.getPrice());
    }

    private List<ItemInfoResponse> changeAllItemToItemInfoResponse(List<Item> items) {

        List<ItemInfoResponse> responseItems = new ArrayList<>();
        for (Item item : items) {
            responseItems.add(changeItemToItemInfoResponse(item));
        }
        return responseItems;
    }

    private Item changeItemChangeRequestToItem(ItemChangeRequest itemChangeRequest) {

        return new Item(itemChangeRequest.getItemType(), itemChangeRequest.getItemName(), itemChangeRequest.getDuration(), itemChangeRequest.getPrice());
    }

    @GetMapping("/manage")
    public HttpEntity<List<ItemsPerItemType>> getItemCategory() {

        List<ItemsPerItemType> itemsPerItemTypes = itemService.getItemCategory();
        return new ResponseEntity<>(itemsPerItemTypes, HttpStatus.OK);
    }
}
