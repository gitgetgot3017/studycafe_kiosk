package lhj.studycafe_kiosk.domain.seat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MySeatResponse {

    private Long id;
    private String entranceCode;
}
