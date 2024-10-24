package lhj.studycafe_kiosk.order;

import lhj.studycafe_kiosk.order.dto.OrderCancelFailResponse;
import lhj.studycafe_kiosk.order.dto.OrderFailResponse;
import lhj.studycafe_kiosk.order.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = OrderController.class)
public class OrderExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public OrderFailResponse orderFailBasedImproperRequest() {
        return new OrderFailResponse("주문", "필드 검증에 실패하였습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public OrderFailResponse orderFailBasedImproperRequest(ImproperRequestException e) {
        return new OrderFailResponse("주문", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public OrderFailResponse orderFailBasedInvalidCoupon(InvalidCouponException e) {
        return new OrderFailResponse("주문", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public OrderFailResponse orderFailBasedImproperCoupon(InappropriateCouponException e) {
        return new OrderFailResponse("주문", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public OrderFailResponse changeOrderIsUsedFail(IllegalStateException e) {
        return new OrderFailResponse("주문", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public OrderCancelFailResponse alreadyRefundFail(AlreadyRefundException e) {
        return new OrderCancelFailResponse("주문", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public OrderCancelFailResponse impossibleRefundFail(ImpossibleRefundException e) {
        return new OrderCancelFailResponse("주문", e.getMessage());
    }
}
