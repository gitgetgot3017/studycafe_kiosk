package lhj.studycafe_kiosk.domain.seat.exception;

public class NotExistSeatException extends RuntimeException {

    public NotExistSeatException(String message) {
        super(message);
    }
}
