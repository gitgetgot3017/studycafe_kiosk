package lhj.studycafekiosk.domain.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifySmsFailDto {

    private String domain;
    private String message;
}
