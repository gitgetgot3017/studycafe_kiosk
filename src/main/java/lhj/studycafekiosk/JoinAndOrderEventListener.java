package lhj.studycafekiosk;

import lhj.studycafekiosk.domain.coupon.service.CouponService;
import lhj.studycafekiosk.domain.subscription.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JoinAndOrderEventListener {

    private final CouponService couponService;
    private final SubscriptionService subscriptionService;

    @EventListener
    public void handleJoinMemberEvent(JoinMemberEvent event) {
        couponService.issueJoinCoupon(event.getMember());
    }

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        couponService.issueCouponBasedCumulativeAmount(event.getMember());
        subscriptionService.issueOrExtendSubscription(event.getMember(), event.getItem());
    }
}
