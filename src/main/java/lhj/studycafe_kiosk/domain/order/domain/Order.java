package lhj.studycafe_kiosk.domain.order.domain;

import jakarta.persistence.*;
import lhj.studycafe_kiosk.domain.coupon.domain.Coupon;
import lhj.studycafe_kiosk.domain.item.domain.Item;
import lhj.studycafe_kiosk.domain.member.domain.Member;
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
        price *= (double) (100 - refundRate) / 100;
        orderStatus = OrderStatus.CANCELED;
    }

    public void refundPartialConsiderCoupon(int refundRate) {
        int originPaid = item.getPrice() * refundRate / 100;
        int unpaid = item.getPrice() - price;
        int refundPrice =  originPaid - unpaid;
        price -= refundPrice;
        orderStatus = OrderStatus.CANCELED;
    }
}
