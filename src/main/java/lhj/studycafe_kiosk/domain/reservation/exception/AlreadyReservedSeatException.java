package lhj.studycafe_kiosk.domain.reservation.exception;

public class AlreadyReservedSeatException extends RuntimeException {

    public AlreadyReservedSeatException(String message) {
        super(message);
    }
}
