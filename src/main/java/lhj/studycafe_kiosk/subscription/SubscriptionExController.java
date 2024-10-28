package lhj.studycafe_kiosk.subscription;

import lhj.studycafe_kiosk.subscription.dto.SubscriptionChangeFailResponse;
import lhj.studycafe_kiosk.subscription.exception.NotExistSubscriptionException;
import lhj.studycafe_kiosk.subscription.exception.SubscriptionChangeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = SubscriptionController.class)
public class SubscriptionExController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SubscriptionChangeFailResponse changeSubscriptionFail(ServletRequestBindingException e) {
        return new SubscriptionChangeFailResponse("이용권 변경", "필드 검증에 실패하였습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SubscriptionChangeFailResponse notExistSubscriptionFail(NotExistSubscriptionException e) {
        return new SubscriptionChangeFailResponse("이용권 변경", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SubscriptionChangeFailResponse subscriptionChangeFail(SubscriptionChangeException e) {
        return new SubscriptionChangeFailResponse("이용권 변경", e.getMessage());
    }
}
