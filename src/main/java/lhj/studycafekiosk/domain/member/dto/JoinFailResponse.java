package lhj.studycafekiosk.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinFailResponse {

    private String domain;
    private String message;
}
