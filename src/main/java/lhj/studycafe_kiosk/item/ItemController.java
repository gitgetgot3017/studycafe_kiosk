package lhj.studycafe_kiosk.item;

import lhj.studycafe_kiosk.domain.Item;
import lhj.studycafe_kiosk.domain.ItemType;
import lhj.studycafe_kiosk.item.dto.*;
import lhj.studycafe_kiosk.item.exception.DuplicateItemNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @GetMapping
    public HttpEntity<List<ItemInfoResponse>> findItems(@RequestParam("itemType") ItemType itemType) {

        List<Item> items = itemRepository.getItems(itemType);

        if (items.isEmpty()) {
            throw new NoItemException("아직 상품이 등록되지 않았습니다.");
        }

        List<ItemInfoResponse> responseItems = changeAllItemToItemInfoResponse(items);
        return new ResponseEntity<>(responseItems, HttpStatus.OK);
    }

    private void validateDuplicateItemName(String itemName) {

        if (itemService.existItemName(itemName)) {
            throw new DuplicateItemNameException("이미 등록된 상품입니다.");
        }
    }

    private Item changeItemRegRequestToItem(ItemRegRequest itemRegRequest) {

        return new Item(itemRegRequest.getItemType(), itemRegRequest.getItemName(), itemRegRequest.getPrice());
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
}
