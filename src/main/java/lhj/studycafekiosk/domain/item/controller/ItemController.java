package lhj.studycafekiosk.domain.item.controller;

import lhj.studycafekiosk.domain.item.dto.*;
import lhj.studycafekiosk.domain.item.exception.DuplicateItemNameException;
import lhj.studycafekiosk.domain.item.service.ItemService;
import lhj.studycafekiosk.domain.item.domain.Item;
import lhj.studycafekiosk.ItemsPerItemType;
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

    @PostMapping
    public HttpEntity<ItemRegResponse> registerItem(@RequestBody @Validated ItemRegRequest itemRegRequest) {

        Item item = changeItemRegRequestToItem(itemRegRequest);
        itemService.registerItem(item);

        ItemRegResponse itemRegResponse = new ItemRegResponse("상품을 정상적으로 등록하였습니다.", item.getId());
        return new ResponseEntity<>(itemRegResponse, HttpStatus.CREATED);
    }

    @GetMapping("/detail")
    public HttpEntity<List<ItemsPerItemType>> getItemsPerItemType() {

        List<ItemsPerItemType> itemsPerItemTypes = itemService.getItemCategory();
        return new ResponseEntity<>(itemsPerItemTypes, HttpStatus.OK);
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

        return new Item(itemRegRequest.getItemType(), itemRegRequest.getItemName(), itemRegRequest.getUsageTime(), itemRegRequest.getUsagePeriod(), itemRegRequest.getPrice());
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

        return new Item(itemChangeRequest.getItemType(), itemChangeRequest.getItemName(), itemChangeRequest.getUsageTime(), itemChangeRequest.getUsagePeriod(), itemChangeRequest.getPrice());
    }

    @DeleteMapping("/{itemId}")
    public void removeItem(@PathVariable("itemId") Long itemId) {

        itemService.removeItem(itemId);
    }
}
