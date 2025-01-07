package lhj.studycafe_kiosk.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendSnsFailDto {

    private String domain;
    private String message;
}
