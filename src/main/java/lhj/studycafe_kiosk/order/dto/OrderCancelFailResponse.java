package lhj.studycafe_kiosk.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderCancelFailResponse {

    private String domain;
    private String message;
}