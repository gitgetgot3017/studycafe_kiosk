package lhj.studycafe_kiosk.item;

import lhj.studycafe_kiosk.item.dto.FindItemFailResponse;
import lhj.studycafe_kiosk.item.dto.ItemRegFailResponse;
import lhj.studycafe_kiosk.item.exception.NoItemException;
import lhj.studycafe_kiosk.item.exception.DuplicateItemNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(assignableTypes = ItemController.class)
public class ItemExControllerAdvice {

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

    @ResponseStatus(HttpStatus.NOT_FOUND)
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
}
