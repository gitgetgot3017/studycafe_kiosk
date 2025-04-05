package lhj.studycafekiosk.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutResponse {

    private String message;
    private Long id;
}
