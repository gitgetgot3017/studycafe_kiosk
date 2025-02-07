package lhj.studycafe_kiosk.member.dto;

import lhj.studycafe_kiosk.domain.MemberGrade;
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
