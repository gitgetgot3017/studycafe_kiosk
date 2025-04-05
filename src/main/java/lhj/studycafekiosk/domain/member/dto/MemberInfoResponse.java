package lhj.studycafekiosk.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MemberInfoResponse {

    private String name;
    private String phone;
    private LocalDate birth;
}
