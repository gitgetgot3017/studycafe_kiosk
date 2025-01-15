package lhj.studycafe_kiosk.order;

import lhj.studycafe_kiosk.coupon.CouponRepository;
import lhj.studycafe_kiosk.coupon.CouponService;
import lhj.studycafe_kiosk.coupon.exception.NotExistCouponException;
import lhj.studycafe_kiosk.domain.*;
import lhj.studycafe_kiosk.item.ItemRepository;
import lhj.studycafe_kiosk.item.exception.NotExistItemException;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.member.exception.NotExistMemberException;
import lhj.studycafe_kiosk.order.dto.*;
import lhj.studycafe_kiosk.order.exception.AlreadyRefundException;
import lhj.studycafe_kiosk.order.exception.NotDailyJoinException;
import lhj.studycafe_kiosk.order.exception.NotExistOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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

    @PostMapping("/{orderId}")
    public HttpEntity<OrderRefundResponse> cancelOrder(@SessionAttribute("loginMember") Long memberId, @PathVariable("orderId") Long orderId) {

        Order order = orderRepository.getOrder(orderId);
        Member member = memberRepository.getMember(memberId);

        validateAlreadyRefund(order);

        int refundRate = orderService.getRefundRate(order);
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
