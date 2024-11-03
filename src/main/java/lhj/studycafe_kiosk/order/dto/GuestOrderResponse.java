package lhj.studycafe_kiosk.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GuestOrderResponse {

    private Long orderId;
    private String itemName;
    private int orderPrice;
    private LocalDateTime orderDateTime;
}
