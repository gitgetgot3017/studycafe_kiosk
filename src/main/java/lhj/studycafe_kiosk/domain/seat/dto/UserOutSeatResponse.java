package lhj.studycafe_kiosk.domain.seat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserOutSeatResponse {

    private String message;
    private Long id;
    private String remainderTime;
}
