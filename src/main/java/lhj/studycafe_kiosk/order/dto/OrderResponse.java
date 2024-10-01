package lhj.studycafe_kiosk.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String memberName;
    private String itemName;
    private int orderPrice;
    private LocalDateTime orderDateTime;
}
