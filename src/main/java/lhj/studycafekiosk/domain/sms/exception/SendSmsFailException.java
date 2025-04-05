package lhj.studycafekiosk.domain.sms.exception;

public class SendSmsFailException extends RuntimeException {

    public SendSmsFailException(String message) {
        super(message);
    }
}
