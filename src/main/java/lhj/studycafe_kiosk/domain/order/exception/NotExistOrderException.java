package lhj.studycafe_kiosk.domain.order.exception;

public class NotExistOrderException extends RuntimeException {

    public NotExistOrderException(String message) {
        super(message);
    }
}
