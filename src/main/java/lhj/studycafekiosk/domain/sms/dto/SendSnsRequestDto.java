package lhj.studycafekiosk.domain.sms.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SendSnsRequestDto {

    @Pattern(regexp = "^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$")
    private String toPhoneNumber;
}
