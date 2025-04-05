package lhj.studycafekiosk.domain.member.exception;

public class NotExistMemberException extends RuntimeException {

    public NotExistMemberException(String message) {
        super(message);
    }
}
