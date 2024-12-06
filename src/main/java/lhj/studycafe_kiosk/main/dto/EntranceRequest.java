package lhj.studycafe_kiosk.main.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EntranceRequest {

    @Pattern(regexp = "^([0-9]{4})$")
    @NotNull
    private String phone;

    @Pattern(regexp = "^[0-9]{2}$")
    @NotNull
    private String password;
}
