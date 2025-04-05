package lhj.studycafekiosk.domain.sms.exception;

public class VerificationAttemptLimitExceededException extends RuntimeException {

    public VerificationAttemptLimitExceededException(String message) {
        super(message);
    }
}
