package lhj.studycafe_kiosk.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderCancelFailResponse {

    private String domain;
    private String message;
}
