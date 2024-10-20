package lhj.studycafe_kiosk.order;

import lhj.studycafe_kiosk.coupon.CouponRepository;
import lhj.studycafe_kiosk.coupon.CouponService;
import lhj.studycafe_kiosk.coupon.exception.NotExistCouponException;
import lhj.studycafe_kiosk.domain.*;
import lhj.studycafe_kiosk.item.ItemRepository;
import lhj.studycafe_kiosk.item.exception.NotExistItemException;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.order.dto.ChangeOrderIsUsedResponse;
import lhj.studycafe_kiosk.order.dto.OrderRequest;
import lhj.studycafe_kiosk.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final ItemRepository itemRepository;
    private final CouponService couponService;
    private final CouponRepository couponRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @PostMapping
    public HttpEntity<OrderResponse> orderItem(@SessionAttribute("loginMember") Long memberId, @RequestBody @Validated OrderRequest orderRequest) {

        validateOrderRequest(orderRequest);

        Long orderId = orderService.orderItem(memberId, orderRequest.getItemId(), orderRequest.getCouponId());

        Member member = memberRepository.getMember(memberId);
        Item item = itemRepository.getItem(orderRequest.getItemId()).get();
        Order order = orderRepository.getOrder(orderId);
        OrderResponse orderResponse = new OrderResponse(orderId, member.getName(), item.getItemName(), order.getPrice(), LocalDateTime.now());

        handleCouponUsage(orderRequest.getCouponId()); // 쿠폰 사용 완료 처리

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{orderId}")
    public HttpEntity<ChangeOrderIsUsedResponse> startUseRequest(@PathVariable("orderId") Long orderId) {

        Order order = orderRepository.getOrder(orderId);

        validateState(order);
        orderService.changeOrderIsUsed(order);

        ChangeOrderIsUsedResponse changeOrderIsUsedResponse = new ChangeOrderIsUsedResponse("주문", "이용권 사용 시작 처리하였습니다.");
        return new ResponseEntity(changeOrderIsUsedResponse, HttpStatus.ACCEPTED);
    }

    private void validateState(Order order) {

        if (order.getItem().getItemType() == ItemType.DAILY || order.getItem().getItemType() == ItemType.CHARGE || order.isUsed()) {
            throw new IllegalStateException("이미 사용 시작된 이용권입니다.");
        }
    }

    private void validateOrderRequest(OrderRequest orderRequest) {

        Optional<Item> opItem = itemRepository.getItem(orderRequest.getItemId());
        if (opItem.isEmpty()) {
            throw new NotExistItemException("존재하지 않는 상품 ID를 입력하셨습니다.");
        }

        if (orderRequest.getCouponId() == null) { // 주문 시 쿠폰을 사용하지 않는 경우
            return;
        }
        Optional<Coupon> opCoupon = couponRepository.getCoupon(orderRequest.getCouponId());
        if (opCoupon.isEmpty()) {
            throw new NotExistCouponException("존재하지 않는 쿠폰 ID를 입력하셨습니다.");
        }
    }

    private void handleCouponUsage(Long couponId) {

        if (couponId == null) { // 쿠폰을 사용하지 않는 경우
            return;
        }

        couponService.changeCouponStatus(couponId);
    }
}
