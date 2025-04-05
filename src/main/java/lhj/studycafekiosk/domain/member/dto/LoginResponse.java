package lhj.studycafekiosk.domain.member.dto;

import lhj.studycafekiosk.domain.member.domain.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String message;
    private Long id;
    private MemberGrade grade;
    private String redirectUrl;
}
