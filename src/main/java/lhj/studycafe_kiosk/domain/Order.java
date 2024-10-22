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

    private int price;

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private LocalDateTime orderDatetime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Member member, Item item, int price, Coupon coupon, LocalDateTime orderDatetime, OrderStatus orderStatus) {
        this.member = member;
        this.item = item;
        this.price = price;
        this.coupon = coupon;
        this.orderDatetime = orderDatetime;
        this.orderStatus = orderStatus;
    }

    public void refundFull() {
        price = 0;
        orderStatus = OrderStatus.CANCELED;
    }

    public void refundPartial(int refundRate) {
        System.out.println((double) (100 - refundRate) / 100);
        price *= (double) (100 - refundRate) / 100;
        orderStatus = OrderStatus.CANCELED;
    }
}
