package lhj.studycafekiosk.domain.seat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SeatChangeRequest {

    @NotNull
    private Long afterSeatId;
}
