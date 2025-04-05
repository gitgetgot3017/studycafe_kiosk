package lhj.studycafekiosk.domain.sms.exception;

public class VerificationCodeTimeLimitException extends RuntimeException {

    public VerificationCodeTimeLimitException(String message) {
        super(message);
    }
}
