package lhj.studycafekiosk.domain.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class VerificationInfo {

    private String verificationCode;
    private LocalDateTime expiredDateTime;
}
