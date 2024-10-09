package lhj.studycafe_kiosk.seat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatChangeSuccessResponse {

    private String message;
    private Long beforeSeatId;
    private Long afterSeatId;
}
