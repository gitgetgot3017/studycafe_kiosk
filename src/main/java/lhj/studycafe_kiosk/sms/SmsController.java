package lhj.studycafe_kiosk.sms;

import lhj.studycafe_kiosk.sms.dto.SendSnsRequestDto;
import lhj.studycafe_kiosk.sms.dto.SendSnsSuccessDto;
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
}

