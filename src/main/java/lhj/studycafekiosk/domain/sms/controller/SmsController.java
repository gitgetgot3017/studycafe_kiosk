package lhj.studycafekiosk.domain.sms.controller;

import lhj.studycafekiosk.domain.sms.domain.VerificationResult;
import lhj.studycafekiosk.domain.sms.dto.SendSnsRequestDto;
import lhj.studycafekiosk.domain.sms.dto.SendSnsSuccessDto;
import lhj.studycafekiosk.domain.sms.dto.VerifySmsRequestDto;
import lhj.studycafekiosk.domain.sms.dto.VerifySmsSuccessDto;
import lhj.studycafekiosk.domain.sms.exception.VerificationCodeMismatchException;
import lhj.studycafekiosk.domain.sms.exception.VerificationCodeTimeLimitException;
import lhj.studycafekiosk.domain.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lhj.studycafekiosk.domain.sms.domain.VerificationResult.MISMATCH;
import static lhj.studycafekiosk.domain.sms.domain.VerificationResult.TIME_EXPIRED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;

    @PostMapping
    public HttpEntity<SendSnsSuccessDto> sendSms(@RequestBody @Validated SendSnsRequestDto sendSnsRequestDto) {

        smsService.sendSms(sendSnsRequestDto.getToPhoneNumber());

        SendSnsSuccessDto sendSnsSuccessDto = new SendSnsSuccessDto("인증번호가 전송되었습니다.");
        return new ResponseEntity<>(sendSnsSuccessDto, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public HttpEntity<VerifySmsSuccessDto> verifyCode(@RequestBody @Validated VerifySmsRequestDto verifySmsRequestDto) {

        VerificationResult verificationResult = smsService.verifyCode(verifySmsRequestDto.getPhone(), verifySmsRequestDto.getVerificationCode());
        if (verificationResult == TIME_EXPIRED) {
            throw new VerificationCodeTimeLimitException("인증 시간이 경과하였습니다.");
        } else if (verificationResult == MISMATCH) {
            throw new VerificationCodeMismatchException("인증번호를 잘못 입력하셨습니다.");
        }

        VerifySmsSuccessDto verifySnsSuccessDto = new VerifySmsSuccessDto("번호 인증에 성공하셨습니다.");
        return new ResponseEntity<>(verifySnsSuccessDto, HttpStatus.OK);
    }
}

