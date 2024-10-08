package lhj.studycafe_kiosk.coupon;

import lhj.studycafe_kiosk.member.JoinMemberEvent;
import lhj.studycafe_kiosk.order.OrderEvent;
import lhj.studycafe_kiosk.subscription.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CouponEventListener {

    private final CouponService couponService;
    private final SubscriptionService subscriptionService;

    @EventListener
    public void handleJoinMemberEvent(JoinMemberEvent event) {
        couponService.issueJoinCoupon(event.getMember());
    }

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        couponService.issueCouponBasedCumulativeAmount(event.getMember());
        subscriptionService.issueSubscription(event.getMember(), event.getItem(), event.getOrder());
    }
}
