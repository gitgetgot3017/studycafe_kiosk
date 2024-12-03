package lhj.studycafe_kiosk.subscription;

import lhj.studycafe_kiosk.member.exception.NotLoginException;
import lhj.studycafe_kiosk.subscription.dto.SubscriptionFailResponse;
import lhj.studycafe_kiosk.subscription.exception.NotExistSubscriptionException;
import lhj.studycafe_kiosk.subscription.exception.SubscriptionChangeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = SubscriptionController.class)
public class SubscriptionExController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SubscriptionFailResponse notExistSubscriptionFail(NotExistSubscriptionException e) {
        return new SubscriptionFailResponse("이용권", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SubscriptionFailResponse subscriptionChangeFail(SubscriptionChangeException e) {
        return new SubscriptionFailResponse("이용권 변경", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SubscriptionFailResponse subscriptionChangeFail(NotLoginException e) {
        return new SubscriptionFailResponse("로그인", e.getMessage());
    }
}
