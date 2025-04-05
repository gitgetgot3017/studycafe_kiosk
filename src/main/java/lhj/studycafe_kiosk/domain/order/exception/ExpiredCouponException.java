package lhj.studycafe_kiosk.domain.order.exception;

public class ExpiredCouponException extends InvalidCouponException {

    public ExpiredCouponException(String message) {
        super(message);
    }
}
