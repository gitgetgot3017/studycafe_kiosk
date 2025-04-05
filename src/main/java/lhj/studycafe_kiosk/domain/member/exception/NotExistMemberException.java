package lhj.studycafe_kiosk.domain.member.exception;

public class NotExistMemberException extends RuntimeException {

    public NotExistMemberException(String message) {
        super(message);
    }
}
