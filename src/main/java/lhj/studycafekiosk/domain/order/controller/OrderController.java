package lhj.studycafekiosk.domain.order.controller;

import lhj.studycafekiosk.domain.coupon.domain.Coupon;
import lhj.studycafekiosk.domain.coupon.exception.NotExistCouponException;
import lhj.studycafekiosk.domain.coupon.repository.CouponRepository;
import lhj.studycafekiosk.domain.coupon.service.CouponService;
import lhj.studycafekiosk.domain.item.domain.Item;
import lhj.studycafekiosk.domain.item.domain.ItemType;
import lhj.studycafekiosk.domain.item.exception.NotExistItemException;
import lhj.studycafekiosk.domain.item.repository.ItemRepository;
import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.member.exception.NotExistMemberException;
import lhj.studycafekiosk.domain.member.repository.MemberRepository;
import lhj.studycafekiosk.domain.order.domain.Order;
import lhj.studycafekiosk.domain.order.domain.OrderStatus;
import lhj.studycafekiosk.domain.order.dto.OrderHistoryResponse;
import lhj.studycafekiosk.domain.order.dto.OrderRefundResponse;
import lhj.studycafekiosk.domain.order.dto.OrderRequest;
import lhj.studycafekiosk.domain.order.exception.AlreadyExistSubscriptionException;
import lhj.studycafekiosk.domain.order.exception.AlreadyRefundException;
import lhj.studycafekiosk.domain.order.repository.OrderRepository;
import lhj.studycafekiosk.domain.order.service.OrderService;
import lhj.studycafekiosk.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    private final SubscriptionRepository subscriptionRepository;

    @PostMapping
    public void orderItem(@SessionAttribute("loginMember") Long memberId, @RequestBody @Validated OrderRequest orderRequest) {

        Item item = validateOrderRequest(orderRequest);
        validateOrderable(memberId, item);

        orderService.orderItem(memberId, orderRequest.getItemId(), orderRequest.getCouponId());

        handleCouponUsage(orderRequest.getCouponId()); // 쿠폰 사용 완료 처리
    }

    private Item validateOrderRequest(OrderRequest orderRequest) {

        Optional<Item> opItem = itemRepository.getItem(orderRequest.getItemId());
        if (opItem.isEmpty()) {
            throw new NotExistItemException("존재하지 않는 상품 ID를 입력하셨습니다.");
        }

        if (orderRequest.getCouponId() != null) { // 주문 시 쿠폰을 사용하는 경우 (쿠폰을 사용하는 경우: null)
            Optional<Coupon> opCoupon = couponRepository.getCoupon(orderRequest.getCouponId());
            if (opCoupon.isEmpty()) {
                throw new NotExistCouponException("존재하지 않는 쿠폰 ID를 입력하셨습니다.");
            }
        }

        return opItem.get();
    }

    private void validateOrderable(Long memberId, Item item) {

        Member member = memberRepository.getMember(memberId);
        try {
            Order order = orderRepository.getItemByMember(member);
            if (order.getItem().getItemType() == item.getItemType()) { // 이용 연장하는 경우 (일일권, 기간권, 고정석에 한함)
                if (item.getItemType() == ItemType.CHARGE) {
                    throw new AlreadyExistSubscriptionException("이미 사용 중인 이용권이 존재합니다.");
                }
            } else { // 다른 종류의 이용권을 구매하는 경우
                throw new AlreadyExistSubscriptionException("이미 사용 중인 이용권이 존재합니다.");
            }
        } catch (EmptyResultDataAccessException e) {
            // 이용권이 존재하지 않는 유저인 경우: 이용권을 구매한다.
        }
    }

    @PostMapping("/{orderId}")
    public HttpEntity<OrderRefundResponse> cancelOrder(@SessionAttribute("loginMember") Long memberId, @PathVariable("orderId") Long orderId) {

        Order order = orderRepository.getOrder(orderId);
        Member member = memberRepository.getMember(memberId);

        validateAlreadyRefund(order);

        int refundRate = orderService.getRefundRate(order, member);
        orderService.getRefundConsiderCoupon(member, order, refundRate);
        if (refundRate == 0) {
            return new ResponseEntity(new OrderRefundResponse("주문취소", "환불이 불가합니다."), HttpStatus.OK);
        } else {
            return new ResponseEntity(new OrderRefundResponse("주문취소", refundRate + "% 환불 가능합니다."), HttpStatus.OK);
        }
    }

    @GetMapping
    public HttpEntity<List<OrderHistoryResponse>> showOrders(@SessionAttribute("loginMember") Long memberId) {

        Member member = memberRepository.getMember(memberId);
        if (member == null) {
            throw new NotExistMemberException("존재하지 않는 회원입니다.");
        }

        List<Order> orders = orderRepository.getOrders(member);
        List<OrderHistoryResponse> orderHistoryResponses;
        if (orders.isEmpty()) {
            orderHistoryResponses = new ArrayList<>();
        } else {
            orderHistoryResponses = changeAllOrderToOrderHistoryResponse(orders);
        }
        return new ResponseEntity<>(orderHistoryResponses, HttpStatus.OK);
    }

    private void handleCouponUsage(Long couponId) {

        if (couponId == null) { // 쿠폰을 사용하지 않는 경우
            return;
        }

        couponService.changeCouponStatus(couponId);
    }

    private void validateAlreadyRefund(Order order) {
        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            throw new AlreadyRefundException("이미 환불된 이용권입니다.");
        }
    }

    private List<OrderHistoryResponse> changeAllOrderToOrderHistoryResponse(List<Order> orders) {
        List<OrderHistoryResponse> orderHistoryResponses = new ArrayList<>();
        for (Order order : orders) {
            orderHistoryResponses.add(changeOrderToOrderHistoryResponse(order));
        }
        return orderHistoryResponses;
    }

    private OrderHistoryResponse changeOrderToOrderHistoryResponse(Order order) {
        return new OrderHistoryResponse(
                order.getId(),
                order.getItem().getItemName(),
                order.getPrice(),
                getFormattedEndDateTime(order.getOrderDatetime()));
    }

    private String getFormattedEndDateTime(LocalDateTime dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        return dateTime.format(formatter);
    }
}
