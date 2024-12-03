package lhj.studycafe_kiosk.order.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderHistoryResponse {

    private Long orderId;
    private String itemName;
    private int orderPrice;
    private String orderDatetime;

    public OrderHistoryResponse(Long orderId, String itemName, int orderPrice, String orderDatetime) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.orderDatetime = orderDatetime;
    }
}
