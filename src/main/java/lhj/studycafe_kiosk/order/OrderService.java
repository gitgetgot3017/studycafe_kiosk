package lhj.studycafe_kiosk.order;

import lhj.studycafe_kiosk.coupon.CouponRepository;
import lhj.studycafe_kiosk.domain.*;
import lhj.studycafe_kiosk.item.ItemRepository;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.order.exception.ExpiredCouponException;
import lhj.studycafe_kiosk.order.exception.InappropriateCouponException;
import lhj.studycafe_kiosk.order.exception.NotYetCouponException;
import lhj.studycafe_kiosk.order.exception.UsedCouponException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Long orderItem(Long memberId, Long itemId, Long couponId) {

        Member member = memberRepository.getMember(memberId);
        Item item = itemRepository.getItem(itemId).get();
        Coupon coupon = couponRepository.getCoupon(couponId).get();

        validateUsableCoupon(coupon); // 사용 가능한 쿠폰인지 검증

        int orderPrice = calculateOrderPrice(item.getPrice(), item, coupon);

        boolean isUsed = true;
        if (item.getItemType() == ItemType.PERIOD || item.getItemType() == ItemType.FIXED) {
            isUsed = false;
        }
        Order order = new Order(member, item, isUsed, orderPrice, LocalDateTime.now());
        eventPublisher.publishEvent(new OrderEvent(this, member)); // 주문 이벤트 발생
        return orderRepository.saveOrder(order);
    }

    public void changeOrderIsUsed(Order order) {
        orderRepository.updateOrderIsUsed(order);
    }

    private void validateUsableCoupon(Coupon coupon) {

        if (coupon.getId() == null) { // 쿠폰을 사용하지 않은 경우 검증 대상이 아님
            return;
        }

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

        if (coupon.getId() == null) { // 쿠폰을 사용하지 않는 경우
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
}
