package lhj.studycafe_kiosk.coupon;

import lhj.studycafe_kiosk.coupon.dto.FindCouponFailResponse;
import lhj.studycafe_kiosk.coupon.exception.NotExistCouponException;
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
