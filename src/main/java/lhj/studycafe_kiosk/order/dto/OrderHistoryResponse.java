package lhj.studycafe_kiosk.order.dto;

import lhj.studycafe_kiosk.domain.Item;

import java.time.LocalDateTime;

public class OrderHistoryResponse {

    private Long orderId;
    private String memberName;
    private String itemName;
    private int orderPrice;
    private String couponName;
    private LocalDateTime orderDatetime;

    public OrderHistoryResponse(Long orderId, String memberName, String itemName, int orderPrice, String couponName, LocalDateTime orderDatetime) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.couponName = couponName;
        this.orderDatetime = orderDatetime;
    }
}
