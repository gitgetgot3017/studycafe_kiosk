package lhj.studycafekiosk.domain.reservation.exception;

public class AlreadyReservedSeatException extends RuntimeException {

    public AlreadyReservedSeatException(String message) {
        super(message);
    }
}
