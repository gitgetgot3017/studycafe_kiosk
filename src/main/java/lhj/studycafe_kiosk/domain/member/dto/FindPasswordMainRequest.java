package lhj.studycafe_kiosk.domain.member.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class FindPasswordMainRequest {

    private String uuid;

    @Pattern(regexp = "^[0-9]{6}$")
    private String password;
}
