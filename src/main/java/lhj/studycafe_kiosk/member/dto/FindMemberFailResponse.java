package lhj.studycafe_kiosk.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindMemberFailResponse {

    private String domain;
    private String message;
}
