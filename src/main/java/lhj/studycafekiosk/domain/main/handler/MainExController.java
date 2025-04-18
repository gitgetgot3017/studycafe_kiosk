package lhj.studycafekiosk.domain.main.handler;

import lhj.studycafekiosk.domain.main.controller.MainController;
import lhj.studycafekiosk.domain.main.dto.EntranceFailResponse;
import lhj.studycafekiosk.domain.member.exception.LoginFailException;
import lhj.studycafekiosk.domain.seat.exception.NotExistSeatException;
import lhj.studycafekiosk.domain.subscription.exception.NotExistSubscriptionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = MainController.class)
public class MainExController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({LoginFailException.class, NotExistSubscriptionException.class, NotExistSeatException.class})
    public EntranceFailResponse notExistMember(RuntimeException e) {
        return new EntranceFailResponse("입실", e.getMessage());
    }
}
