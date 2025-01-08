package lhj.studycafe_kiosk.sms;

import lhj.studycafe_kiosk.sms.dto.SendSnsRequestDto;
import lhj.studycafe_kiosk.sms.dto.SendSnsSuccessDto;
import lhj.studycafe_kiosk.sms.dto.VerifySmsRequestDto;
import lhj.studycafe_kiosk.sms.dto.VerifySmsSuccessDto;
import lhj.studycafe_kiosk.sms.exception.VerificationCodeMismatchException;
import lhj.studycafe_kiosk.sms.exception.VerificationCodeTimeLimitException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

        int verificationResult = smsService.verifyCode(verifySmsRequestDto.getPhone(), verifySmsRequestDto.getVerificationCode());
        if (verificationResult == -1) {
            throw new VerificationCodeMismatchException("인증번호를 잘못 입력하셨습니다.");
        } else if (verificationResult == -2) {
            throw new VerificationCodeTimeLimitException("인증 시간이 경과하였습니다.");
        }

        VerifySmsSuccessDto verifySnsSuccessDto = new VerifySmsSuccessDto("번호 인증에 성공하셨습니다.");
        return new ResponseEntity<>(verifySnsSuccessDto, HttpStatus.OK);
    }
}

