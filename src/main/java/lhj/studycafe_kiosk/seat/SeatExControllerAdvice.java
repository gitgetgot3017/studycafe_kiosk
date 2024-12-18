package lhj.studycafe_kiosk.seat;

import lhj.studycafe_kiosk.seat.dto.SeatChooseFailResponse;
import lhj.studycafe_kiosk.seat.dto.UserOutFailResponse;
import lhj.studycafe_kiosk.seat.exception.EmptySeatOutException;
import lhj.studycafe_kiosk.seat.exception.InvalidSeatChangeException;
import lhj.studycafe_kiosk.seat.exception.NotExistSeatException;
import lhj.studycafe_kiosk.seat.exception.NotUsableSeatException;
import lhj.studycafe_kiosk.subscription.exception.ExpiredSubscriptionException;
import lhj.studycafe_kiosk.subscription.exception.NotExistSubscriptionException;
import lhj.studycafe_kiosk.usage_status.exception.UserNotInException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = SeatController.class)
public class SeatExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SeatChooseFailResponse notExistSeatFail(NotExistSeatException e) {
        return new SeatChooseFailResponse("좌석", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SeatChooseFailResponse occupiedSeatFail(NotUsableSeatException e) {
        return new SeatChooseFailResponse("좌석", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SeatChooseFailResponse notExistSubscriptionFail(NotExistSubscriptionException e) {
        return new SeatChooseFailResponse("좌석", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SeatChooseFailResponse expiredSubscriptionFail(ExpiredSubscriptionException e) {
        return new SeatChooseFailResponse("좌석", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SeatChooseFailResponse invalidSeatChangeFail(InvalidSeatChangeException e) {
        return new SeatChooseFailResponse("좌석", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public UserOutFailResponse userNotInFail(UserNotInException e) {
        return new UserOutFailResponse("좌석", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public UserOutFailResponse emptySeatOutFail(EmptySeatOutException e) {
        return new UserOutFailResponse("좌석", e.getMessage());
    }
}
