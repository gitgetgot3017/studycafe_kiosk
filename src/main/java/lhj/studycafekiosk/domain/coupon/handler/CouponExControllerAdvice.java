package lhj.studycafekiosk.domain.coupon.handler;

import lhj.studycafekiosk.domain.coupon.controller.CouponController;
import lhj.studycafekiosk.domain.coupon.dto.FindCouponFailResponse;
import lhj.studycafekiosk.domain.coupon.exception.NotExistCouponException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = CouponController.class)
public class CouponExControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public FindCouponFailResponse method(NotExistCouponException e) {
        return new FindCouponFailResponse("쿠폰조회", e.getMessage());
    }
}
