package lhj.studycafe_kiosk.order;

import lhj.studycafe_kiosk.coupon.CouponRepository;
import lhj.studycafe_kiosk.domain.*;
import lhj.studycafe_kiosk.item.ItemRepository;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.order.exception.*;
import lhj.studycafe_kiosk.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Long orderItem(Long memberId, Long itemId, Long couponId) {

        Member member = memberRepository.getMember(memberId);
        Item item = itemRepository.getItem(itemId).get();
        Coupon coupon;
        if (couponId == null) { // 쿠폰을 사용하지 않는 경우
            coupon = null;
        } else {
            coupon = couponRepository.getCoupon(couponId).get();
            validateUsableCoupon(coupon); // 사용 가능한 쿠폰인지 검증
        }

        int orderPrice = calculateOrderPrice(item.getPrice(), item, coupon);

        Order order = new Order(member, item, orderPrice, coupon, LocalDateTime.now(), OrderStatus.ORDERED);
        eventPublisher.publishEvent(new OrderEvent(this, member, item, order)); // 주문 이벤트 발생
        return orderRepository.saveOrder(order);
    }

    public Long orderItemGuest(Long itemId, Long couponId) {

        Item item = itemRepository.getItem(itemId).get();

        Order order = new Order(null, item, item.getPrice(), null, LocalDateTime.now(), OrderStatus.ORDERED);
        eventPublisher.publishEvent(new OrderEvent(this, null, item, order)); // 주문 이벤트 발생
        return orderRepository.saveOrder(order);
    }

    public int getRefundRate(Order order) {

        Subscription subscription = subscriptionRepository.getSubscriptionByOrder(order);
        if (subscription.getStartDateTime() == null && checkWithin7Days(order)) {
            return 100; // 100% 환불 가능
        }

        Item item = order.getItem();
        if (item.getItemType() == ItemType.DAILY) {
            return 0; // 환불 불가
        } else if (item.getItemType() == ItemType.CHARGE) {
            if (checkWithin7Days(order) && checkWithin30Percent(order)) {
                return 50; // 50% 환불 가능
            } else {
                return 0; // 환불 불가
            }
        } else {
            if (checkWithin3Days(order)) {
                return 70; // 70% 환불 가능
            } else if (checkWithin7Days(order)) {
                return 50; // 50% 환불 가능
            } else {
                return 0; // 환불 불가
            }
        }
    }

    public void getRefundConsiderCoupon(Member member, Order order, int refundRate) {

        if (refundRate == 0) { // 환불 불가한 경우
            return;
        }

        Coupon coupon = order.getCoupon();
        if (coupon == null) { // 쿠폰을 사용하지 않고 결제한 경우
            if (refundRate == 100) {
                order.refundFull();
            } else if (refundRate > 0) {
                order.refundPartial(refundRate);
            }
        }
        else { // 쿠폰을 사용해서 결제한 경우
            if (coupon.getName().contains("가입")) {
                throw new ImpossibleRefundException("증정된 쿠폰으로 구입한 이용권은 환불 불가합니다.");
            }
            else if (coupon.getName().contains("생일")) {
                if (refundRate == 100) {
                    order.refundFull();
                } else if (refundRate > 0) {
                    order.refundPartialConsiderCoupon(refundRate);
                }
                coupon.reEnableCoupon();
            }
            else if (coupon.getName().contains("구매")) {
                if (refundRate == 100) {
                    order.refundFull();
                } else if (refundRate > 0) {
                    order.refundPartialConsiderCoupon(refundRate);
                }
                decisionReEnableCoupon(member, order, coupon);
            }
        }
        Subscription subscription = subscriptionRepository.getSubscriptionByOrder(order);
        subscription.setSubscriptionInvalid();
    }

    private void validateUsableCoupon(Coupon coupon) {

        LocalDateTime curDateTime = LocalDateTime.now();

        if (coupon.isUsed()) {
            throw new UsedCouponException("이미 사용된 쿠폰입니다.");
        } else if (curDateTime.isAfter(coupon.getEndDatetime())) {
            throw new ExpiredCouponException("사용 기한이 지난 쿠폰입니다.");
        } else if (curDateTime.isBefore(coupon.getStartDatetime())) {
            throw new NotYetCouponException("아직 사용 기한이 아닌 쿠폰입니다.");
        }
    }

    private int calculateOrderPrice(int itemPrice, Item item, Coupon coupon) {

        if (coupon == null) { // 쿠폰을 사용하지 않는 경우
            return itemPrice;
        }

        int rateOrHour = coupon.getRateOrHour();

        if (coupon.getCouponType() == CouponType.RATE) {
            return itemPrice * (100 - rateOrHour) / 100;
        } else {
            validateAppropriateCoupon(item, coupon);
            return 0;
        }
    }

    private void validateAppropriateCoupon(Item item, Coupon coupon) {

        if (!item.getItemName().contains(String.valueOf(coupon.getRateOrHour()))) {
            throw new InappropriateCouponException("쿠폰 적용 대상이 아닙니다.");
        }
    }

    private boolean checkWithin30Percent(Order order) {

        Subscription subscription = subscriptionRepository.getSubscriptionByOrder(order);
        Item item = subscription.getOrder().getItem();

        long itemSecond = getItemSecond(item);
        long leftSecond = getLeftSecond(item, subscription);
        return (itemSecond - leftSecond) / itemSecond * 100 < 30;
    }

    private long getItemSecond(Item item) {

        if (item.getItemType() == ItemType.DAILY || item.getItemType() == ItemType.CHARGE) {
            return item.getUsageTime() * 60 * 60;
        } else {
            return item.getUsagePeriod() * 24 * 60 * 60;
        }
    }

    private long getLeftSecond(Item item, Subscription subscription) {

        if (item.getItemType() == ItemType.CHARGE) {
            return subscription.getLeftTime().getSeconds();
        } else {
            return Duration.between(subscription.getStartDateTime(), subscription.getEndDateTime()).getSeconds();
        }
    }

    private boolean checkWithin7Days(Order order) {
        return LocalDateTime.now().minusDays(7).isBefore(order.getOrderDatetime());
    }

    private boolean checkWithin3Days(Order order) {
        return LocalDateTime.now().minusDays(3).isBefore(order.getOrderDatetime());
    }

    private void decisionReEnableCoupon(Member member, Order order, Coupon coupon) {

        Optional<Long> opCumulativeAmount = orderRepository.getCumulativeAmount(member);
        Long cumulativeAmount = 0L;
        if (opCumulativeAmount.isPresent()) {
            cumulativeAmount = opCumulativeAmount.get();
        }

        int orderPrice = order.getPrice();
        int exceptOrderPrice = cumulativeAmount.intValue() - orderPrice;

        int stdDiscountRate = 0;
        if (300_000 <= orderPrice) {
            if (orderPrice < 500_000) {
                stdDiscountRate = 3;
            } else if (orderPrice < 1_000_000) {
                stdDiscountRate = 5;
            } else if (orderPrice < 1_500_000) {
                stdDiscountRate = 7;
            } else if (stdDiscountRate < 2_000_000) {
                stdDiscountRate = 10;
            } else {
                stdDiscountRate = 15;
            }
        }

        int discountRate = 0;
        if (300_000 <= exceptOrderPrice) {
            if (exceptOrderPrice < 500_000) {
                discountRate = 3;
            } else if (exceptOrderPrice < 1_000_000) {
                discountRate = 5;
            } else if (exceptOrderPrice < 1_500_000) {
                discountRate = 7;
            } else if (exceptOrderPrice < 2_000_000) {
                discountRate = 10;
            } else {
                discountRate = 15;
            }
        }

        coupon.reEnableCoupon();
        if (discountRate != stdDiscountRate) { // 쿠폰 발급 취소
            Coupon specificCoupon = couponRepository.getSpecificCoupon(member, stdDiscountRate);
            specificCoupon.disableCoupon();
        }
    }
}
