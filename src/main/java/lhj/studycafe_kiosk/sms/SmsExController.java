package lhj.studycafe_kiosk.sms;

import lhj.studycafe_kiosk.item.dto.ItemRegFailResponse;
import lhj.studycafe_kiosk.sms.dto.SendSnsFailDto;
import lhj.studycafe_kiosk.sms.exception.SendSmsFailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = SmsController.class)
public class SmsExController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ItemRegFailResponse validatePhoneFail() {
        return new ItemRegFailResponse("번호 인증", "휴대폰 번호는 필수로 입력하셔야 합니다.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public SendSnsFailDto sendSmsFail(SendSmsFailException e) {
        return new SendSnsFailDto("번호 인증", e.getMessage());
    }
}
