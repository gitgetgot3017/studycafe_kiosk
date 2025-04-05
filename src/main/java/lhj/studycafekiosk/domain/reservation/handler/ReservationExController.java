package lhj.studycafekiosk.domain.reservation.handler;

import lhj.studycafekiosk.domain.reservation.controller.ReservationController;
import lhj.studycafekiosk.domain.reservation.dto.DuplicateReservationException;
import lhj.studycafekiosk.domain.reservation.dto.InvalidReservationResponse;
import lhj.studycafekiosk.domain.reservation.exception.AlreadyReservedSeatException;
import lhj.studycafekiosk.domain.reservation.exception.ReservationNotPossibleException;
import lhj.studycafekiosk.domain.seat.exception.EmptySeatOutException;
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
