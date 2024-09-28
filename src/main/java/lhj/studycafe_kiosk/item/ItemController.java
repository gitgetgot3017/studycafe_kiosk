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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ItemRegFailResponse itemFieldValidationFail() {
        return new ItemRegFailResponse("상품", "필드 검증에 실패하였습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ItemRegFailResponse regItemFail(DuplicateItemNameException e) {
        return new ItemRegFailResponse("상품등록", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public FindItemFailResponse findItemsFail1(NoItemException e) {
        return new FindItemFailResponse("상품조회", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public FindItemFailResponse findItemsFail2() {
        return new FindItemFailResponse("상품조회", "파라미터 입력은 필수입니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public FindItemFailResponse findItemsFail3() {
        return new FindItemFailResponse("상품조회", "잘못된 파라미터를 입력하셨습니다.");
    }

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
