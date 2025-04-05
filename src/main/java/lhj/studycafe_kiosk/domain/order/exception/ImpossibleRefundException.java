package lhj.studycafe_kiosk.domain.order.exception;

public class ImpossibleRefundException extends RuntimeException {

    public ImpossibleRefundException(String message) {
        super(message);
    }
}
