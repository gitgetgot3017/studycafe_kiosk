package lhj.studycafe_kiosk.domain.sms.exception;

public class VerificationCodeMismatchException extends RuntimeException {

    public VerificationCodeMismatchException(String message) {
        super(message);
    }
}
