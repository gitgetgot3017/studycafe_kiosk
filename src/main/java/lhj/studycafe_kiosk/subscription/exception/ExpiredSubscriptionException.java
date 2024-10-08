package lhj.studycafe_kiosk.subscription.exception;

public class ExpiredSubscriptionException extends RuntimeException {

    public ExpiredSubscriptionException(String message) {
        super(message);
    }
}
