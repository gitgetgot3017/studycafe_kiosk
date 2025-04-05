package lhj.studycafekiosk.domain.seat.handler;

import lhj.studycafekiosk.domain.seat.controller.SeatController;
import lhj.studycafekiosk.domain.seat.dto.SeatChooseFailResponse;
import lhj.studycafekiosk.domain.seat.dto.UserOutFailResponse;
import lhj.studycafekiosk.domain.seat.exception.EmptySeatOutException;
import lhj.studycafekiosk.domain.seat.exception.InvalidSeatChangeException;
import lhj.studycafekiosk.domain.seat.exception.NotExistSeatException;
import lhj.studycafekiosk.domain.seat.exception.NotUsableSeatException;
import lhj.studycafekiosk.domain.subscription.exception.ExpiredSubscriptionException;
import lhj.studycafekiosk.domain.subscription.exception.NotExistSubscriptionException;
import lhj.studycafekiosk.domain.usagestatus.exception.UserNotInException;
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
