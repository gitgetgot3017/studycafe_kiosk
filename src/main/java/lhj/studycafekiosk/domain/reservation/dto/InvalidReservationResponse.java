package lhj.studycafekiosk.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidReservationResponse {

    private String domain;
    private String message;
}
