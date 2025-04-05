package lhj.studycafekiosk.domain.coupon.exception;

import lhj.studycafekiosk.domain.order.exception.ImproperRequestException;

public class NotExistCouponException extends ImproperRequestException {

    public NotExistCouponException(String message) {
        super(message);
    }
}
