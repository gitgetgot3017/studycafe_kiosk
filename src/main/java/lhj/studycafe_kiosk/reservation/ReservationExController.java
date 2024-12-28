package lhj.studycafe_kiosk.reservation;

import lhj.studycafe_kiosk.reservation.dto.DuplicateReservationException;
import lhj.studycafe_kiosk.reservation.dto.InvalidReservationResponse;
import lhj.studycafe_kiosk.reservation.exception.AlreadyReservedSeatException;
import lhj.studycafe_kiosk.reservation.exception.ReservationNotPossibleException;
import lhj.studycafe_kiosk.seat.exception.EmptySeatOutException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ReservationController.class)
public class ReservationExController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EmptySeatOutException.class, AlreadyReservedSeatException.class, ReservationNotPossibleException.class, DuplicateReservationException.class})
    public InvalidReservationResponse invalidReservationFail(RuntimeException e) {
        return new InvalidReservationResponse("좌석예약", e.getMessage());
    }
}
