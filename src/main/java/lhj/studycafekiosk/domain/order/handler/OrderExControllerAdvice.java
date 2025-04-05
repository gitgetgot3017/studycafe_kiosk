package lhj.studycafekiosk.domain.order.handler;

import lhj.studycafekiosk.domain.member.dto.FindMemberFailResponse;
import lhj.studycafekiosk.domain.member.exception.NotExistMemberException;
import lhj.studycafekiosk.domain.order.controller.OrderController;
import lhj.studycafekiosk.domain.order.dto.GuestsOrderFailResponse;
import lhj.studycafekiosk.domain.order.dto.OrderCancelFailResponse;
import lhj.studycafekiosk.domain.order.dto.OrderFailResponse;
import lhj.studycafekiosk.domain.order.dto.ShowOrderFailResponse;
import lhj.studycafekiosk.domain.order.exception.*;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ShowOrderFailResponse notExistOrderFail(NotExistOrderException e) {
        return new ShowOrderFailResponse("주문", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public FindMemberFailResponse notExistMemberFail(NotExistMemberException e) {
        return new FindMemberFailResponse("주문", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public GuestsOrderFailResponse notDailyJoinFail(NotDailyJoinException e) {
        return new GuestsOrderFailResponse("주문", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public OrderFailResponse alreayExistSubscriptionFail(AlreadyExistSubscriptionException e) {
        return new OrderFailResponse("주문", e.getMessage());
    }
}
