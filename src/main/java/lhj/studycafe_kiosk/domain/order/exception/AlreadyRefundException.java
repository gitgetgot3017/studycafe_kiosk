package lhj.studycafe_kiosk.domain.order.exception;

public class AlreadyRefundException extends RuntimeException {

    public AlreadyRefundException(String message) {
        super(message);
    }
}
