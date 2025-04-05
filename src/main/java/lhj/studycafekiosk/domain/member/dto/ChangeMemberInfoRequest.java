package lhj.studycafekiosk.domain.member.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ChangeMemberInfoRequest {

    private String name;
    private LocalDate birth;
    private String phone;
    private String curPassword;
    private String newPassword;
}
