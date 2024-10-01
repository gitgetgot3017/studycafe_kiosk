package lhj.studycafe_kiosk.coupon;

import lhj.studycafe_kiosk.member.JoinMemberEvent;
import lhj.studycafe_kiosk.order.OrderEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CouponEventListener {

    private final CouponService couponService;

    @EventListener
    public void handleJoinMemberEvent(JoinMemberEvent event) {
        couponService.issueJoinCoupon(event.getMember());
    }

    @EventListener
    public void handleJoinMemberEvent(OrderEvent event) {
        couponService.issueCouponBasedCumulativeAmount(event.getMember());
    }
}
