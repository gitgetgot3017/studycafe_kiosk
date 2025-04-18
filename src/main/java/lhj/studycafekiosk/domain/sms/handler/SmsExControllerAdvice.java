package lhj.studycafekiosk.domain.sms.handler;

import lhj.studycafekiosk.domain.sms.controller.SmsController;
import lhj.studycafekiosk.domain.sms.dto.SendSnsFailDto;
import lhj.studycafekiosk.domain.sms.dto.VerifySmsFailDto;
import lhj.studycafekiosk.domain.sms.exception.SendSmsFailException;
import lhj.studycafekiosk.domain.sms.exception.VerificationAttemptLimitExceededException;
import lhj.studycafekiosk.domain.sms.exception.VerificationCodeMismatchException;
import lhj.studycafekiosk.domain.sms.exception.VerificationCodeTimeLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = SmsController.class)
public class SmsExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public SendSnsFailDto validatePhoneFail() {
        return new SendSnsFailDto("번호 인증", "입력 형식에 맞게 입력해주세요.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public SendSnsFailDto sendSmsFail(SendSmsFailException e) {
        return new SendSnsFailDto("번호 인증", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({VerificationCodeMismatchException.class, VerificationCodeTimeLimitException.class, VerificationAttemptLimitExceededException.class})
    public VerifySmsFailDto verifySmsFail(RuntimeException e) {
        return new VerifySmsFailDto("번호 인증", e.getMessage());
    }
}
