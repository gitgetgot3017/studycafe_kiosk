package lhj.studycafe_kiosk.member.exception;

public class NotExistMemberException extends RuntimeException {

    public NotExistMemberException(String message) {
        super(message);
    }
}
