package lhj.studycafe_kiosk.seat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SeatChangeRequest {

    @NotNull
    private Long beforeSeatId;

    @NotNull
    private Long afterSeatId;
}
