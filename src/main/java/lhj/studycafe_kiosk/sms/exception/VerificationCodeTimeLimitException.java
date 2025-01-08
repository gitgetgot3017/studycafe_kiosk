package lhj.studycafe_kiosk.sms.exception;

public class VerificationCodeTimeLimitException extends RuntimeException {

    public VerificationCodeTimeLimitException(String message) {
        super(message);
    }
}
