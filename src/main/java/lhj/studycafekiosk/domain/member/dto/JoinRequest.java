package lhj.studycafekiosk.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class JoinRequest {

    @Pattern(regexp = "^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$")
    @NotNull
    private String phone;

    @Pattern(regexp = "^[0-9]{6}$")
    private String verificationCode;

    @Pattern(regexp = "^[0-9]{6}$")
    @NotNull
    private String password;

    private boolean optionalClause;
}
