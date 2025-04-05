package lhj.studycafe_kiosk.domain.reservation.dto;

public class DuplicateReservationException extends RuntimeException {

    public DuplicateReservationException(String message) {
        super(message);
    }
}
