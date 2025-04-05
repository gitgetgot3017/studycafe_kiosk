package lhj.studycafe_kiosk.domain.sms.exception;

public class VerificationCodeTimeLimitException extends RuntimeException {

    public VerificationCodeTimeLimitException(String message) {
        super(message);
    }
}
