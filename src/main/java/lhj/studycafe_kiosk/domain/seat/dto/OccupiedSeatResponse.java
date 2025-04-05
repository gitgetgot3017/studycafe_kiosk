package lhj.studycafe_kiosk.domain.seat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OccupiedSeatResponse {

    private long seatId;
    private boolean mySeat;
}
