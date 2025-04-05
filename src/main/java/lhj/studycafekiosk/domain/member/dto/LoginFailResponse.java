package lhj.studycafekiosk.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginFailResponse {

    private String domain;
    private String message;
}
