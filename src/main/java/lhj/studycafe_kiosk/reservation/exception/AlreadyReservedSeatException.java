package lhj.studycafe_kiosk.reservation.exception;

public class AlreadyReservedSeatException extends RuntimeException {

    public AlreadyReservedSeatException(String message) {
        super(message);
    }
}
