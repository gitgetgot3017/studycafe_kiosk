package lhj.studycafe_kiosk.domain.sms.exception;

public class SendSmsFailException extends RuntimeException {

    public SendSmsFailException(String message) {
        super(message);
    }
}
