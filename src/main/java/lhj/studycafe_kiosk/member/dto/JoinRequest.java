package lhj.studycafe_kiosk.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class JoinRequest {

    @NotBlank
    private String name;

    @Pattern(regexp = "^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$")
    @NotNull
    private String phone;

    @Pattern(regexp = "^[0-9]{4}$")
    @NotNull
    private String password;

    @NotNull
    private LocalDate birth;
}