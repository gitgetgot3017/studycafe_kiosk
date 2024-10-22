package lhj.studycafe_kiosk.order.exception;

public class AlreadyRefundException extends RuntimeException {

    public AlreadyRefundException(String message) {
        super(message);
    }
}
