package lhj.studycafe_kiosk.usage_status;

import lhj.studycafe_kiosk.usage_status.dto.UsageStatusFailResponse;
import lhj.studycafe_kiosk.usage_status.exception.NotExistUserInOutException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = UsageStatusController.class)
public class UsageStatusExController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public UsageStatusFailResponse notExistUserInOutFail(NotExistUserInOutException e) {
        return new UsageStatusFailResponse("입퇴실 조회", e.getMessage());
    }
}
