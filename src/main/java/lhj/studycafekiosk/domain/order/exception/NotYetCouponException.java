package lhj.studycafekiosk.domain.order.exception;

public class NotYetCouponException extends InvalidCouponException {

    public NotYetCouponException(String message) {
        super(message);
    }
}
