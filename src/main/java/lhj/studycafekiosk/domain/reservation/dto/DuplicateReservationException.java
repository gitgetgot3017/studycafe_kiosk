package lhj.studycafekiosk.domain.reservation.dto;

public class DuplicateReservationException extends RuntimeException {

    public DuplicateReservationException(String message) {
        super(message);
    }
}
