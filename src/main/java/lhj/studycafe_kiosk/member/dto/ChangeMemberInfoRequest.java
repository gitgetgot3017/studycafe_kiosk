package lhj.studycafe_kiosk.member.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
public class ChangeMemberInfoRequest {

    private String name;
    private LocalDate birth;
    private String phone;
    private String curPassword;
    private String newPassword;
}
