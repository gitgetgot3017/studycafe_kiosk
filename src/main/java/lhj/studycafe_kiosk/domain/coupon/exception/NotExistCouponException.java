package lhj.studycafe_kiosk.domain.coupon.exception;

import lhj.studycafe_kiosk.domain.order.exception.ImproperRequestException;

public class NotExistCouponException extends ImproperRequestException {

    public NotExistCouponException(String message) {
        super(message);
    }
}
