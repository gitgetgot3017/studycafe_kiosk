package lhj.studycafekiosk.domain.order.exception;

public class NotExistOrderException extends RuntimeException {

    public NotExistOrderException(String message) {
        super(message);
    }
}
