package lhj.studycafe_kiosk.reservation.dto;

public class DuplicateReservationException extends RuntimeException {

    public DuplicateReservationException(String message) {
        super(message);
    }
}
