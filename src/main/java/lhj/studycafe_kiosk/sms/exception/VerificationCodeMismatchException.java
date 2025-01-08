package lhj.studycafe_kiosk.sms.exception;

public class VerificationCodeMismatchException extends RuntimeException {

    public VerificationCodeMismatchException(String message) {
        super(message);
    }
}
