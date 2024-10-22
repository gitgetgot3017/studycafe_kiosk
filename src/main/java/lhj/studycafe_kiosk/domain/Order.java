package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private boolean isUsed;

    private int price;

    private LocalDateTime orderDatetime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Member member, Item item, boolean isUsed, int price, LocalDateTime orderDatetime) {
        this.member = member;
        this.item = item;
        this.isUsed = isUsed;
        this.price = price;
        this.orderDatetime = orderDatetime;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public void refundFull() {
        price = 0;
        orderStatus = OrderStatus.CANCELED;
    }

    public void refundPartial(int refundRate) {
        price *= (100 - refundRate) / 100;
        orderStatus = OrderStatus.CANCELED;
    }
}
