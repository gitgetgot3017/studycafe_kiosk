package lhj.studycafe_kiosk.domain.member.exception;

public class DuplicatePhoneException extends RuntimeException {

    public DuplicatePhoneException(String message) {
        super(message);
    }

}
