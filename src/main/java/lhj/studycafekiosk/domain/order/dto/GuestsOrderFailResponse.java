package lhj.studycafekiosk.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GuestsOrderFailResponse {

    private String domain;
    private String message;
}
