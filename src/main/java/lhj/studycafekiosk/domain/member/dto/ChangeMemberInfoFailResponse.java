package lhj.studycafekiosk.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeMemberInfoFailResponse {

    private String domain;
    private String message;
}
