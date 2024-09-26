package lhj.studycafe_kiosk.member.exception;

public class LoginFailException extends RuntimeException {

    public LoginFailException(String message) {
        super(message);
    }
}
