package lhj.studycafe_kiosk.order.exception;

public class ExpiredCouponException extends InvalidCouponException {

    public ExpiredCouponException(String message) {
        super(message);
    }
}
