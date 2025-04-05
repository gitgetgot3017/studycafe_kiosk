package lhj.studycafekiosk.domain.member.exception;

public class DuplicatePhoneException extends RuntimeException {

    public DuplicatePhoneException(String message) {
        super(message);
    }

}
