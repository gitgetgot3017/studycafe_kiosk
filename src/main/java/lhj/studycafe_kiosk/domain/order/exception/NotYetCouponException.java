package lhj.studycafe_kiosk.domain.order.exception;

public class NotYetCouponException extends InvalidCouponException {

    public NotYetCouponException(String message) {
        super(message);
    }
}
