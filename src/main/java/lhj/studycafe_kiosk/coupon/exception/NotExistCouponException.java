package lhj.studycafe_kiosk.coupon.exception;

import lhj.studycafe_kiosk.order.exception.ImproperRequestException;

public class NotExistCouponException extends ImproperRequestException {

    public NotExistCouponException(String message) {
        super(message);
    }
}
