package lhj.studycafe_kiosk.item;

import lhj.studycafe_kiosk.domain.Item;
import lhj.studycafe_kiosk.item.dto.*;
import lhj.studycafe_kiosk.item.exception.DuplicateItemNameException;
import lhj.studycafe_kiosk.item.exception.NotExistItemException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ItemRegFailResponse joinFail() {
        return new ItemRegFailResponse("상품", "필드 검증에 실패하였습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ItemRegFailResponse regItemFail(DuplicateItemNameException e) {
        return new ItemRegFailResponse("상품등록", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public FindItemFailResponse findItemFail(NotExistItemException e) {
        return new FindItemFailResponse("상품조회", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public FindItemFailResponse findItemsFail(NoItemException e) {
        return new FindItemFailResponse("상품조회", e.getMessage());
    }

    @PostMapping
    public HttpEntity<ItemRegResponse> registerItem(@RequestBody @Validated ItemRegRequest itemRegRequest) {

        validateDuplicateItemName(itemRegRequest.getItemName());

        Item item = changeItemRegRequestToItem(itemRegRequest);
        itemService.registerItem(item);

        ItemRegResponse itemRegResponse = new ItemRegResponse("상품을 정상적으로 등록하였습니다.", item.getId());
        return new ResponseEntity<>(itemRegResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{itemId}")
    public HttpEntity<ItemInfoResponse> findItem(@PathVariable("itemId") Long itemId) {

        Item item = itemRepository.getItem(itemId);
        if (item == null) {
            throw new NotExistItemException("등록되지 않은 상품입니다.");
        }

        ItemInfoResponse itemInfoResponse = changeItemInfoResponseToItem(item);
        return new ResponseEntity<>(itemInfoResponse, HttpStatus.OK);
    }

    @GetMapping
    public HttpEntity<List<Item>> findItems() {

        List<Item> items = itemRepository.getItems();
        if (items.isEmpty()) {
            throw new NoItemException("해당 상품이 존재하지 않습니다.");
        }

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    private void validateDuplicateItemName(String itemName) {

        if (itemService.existItemName(itemName)) {
            throw new DuplicateItemNameException("이미 등록된 상품입니다.");
        }
    }

    private Item changeItemRegRequestToItem(ItemRegRequest itemRegRequest) {

        return new Item(itemRegRequest.getItemType(), itemRegRequest.getItemName(), itemRegRequest.getPrice());
    }

    private ItemInfoResponse changeItemInfoResponseToItem(Item item) {

        return new ItemInfoResponse(item.getItemType(), item.getItemName(), item.getPrice());
    }
}
