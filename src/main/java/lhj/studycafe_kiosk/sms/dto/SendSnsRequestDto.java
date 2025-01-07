package lhj.studycafe_kiosk.sms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SendSnsRequestDto {

    @NotBlank
    private String toPhoneNumber;
}
