package lhj.studycafekiosk.domain.member.exception;

public class NotLoginException extends RuntimeException {

    public NotLoginException(String message) {
        super(message);
    }
}
