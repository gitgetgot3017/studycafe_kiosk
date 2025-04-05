package lhj.studycafe_kiosk.domain.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifySmsFailDto {

    private String domain;
    private String message;
}
