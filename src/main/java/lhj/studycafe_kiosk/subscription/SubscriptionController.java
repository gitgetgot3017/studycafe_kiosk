package lhj.studycafe_kiosk.subscription;

import lhj.studycafe_kiosk.domain.Item;
import lhj.studycafe_kiosk.domain.ItemType;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Subscription;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.order.dto.ChangeOrderIsUsedResponse;
import lhj.studycafe_kiosk.subscription.dto.RepresentativeSubscriptionResponse;
import lhj.studycafe_kiosk.subscription.dto.SubscriptionChangeRequest;
import lhj.studycafe_kiosk.subscription.dto.SubscriptionChangeResponse;
import lhj.studycafe_kiosk.subscription.dto.SubscriptionListResponse;
import lhj.studycafe_kiosk.subscription.exception.NotExistSubscriptionException;
import lhj.studycafe_kiosk.subscription.exception.SubscriptionChangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;

    @PatchMapping("/{subscriptionId}")
    public HttpEntity<ChangeOrderIsUsedResponse> startUseRequest(@PathVariable("subscriptionId") Long subscriptionId) {

        Subscription subscription = subscriptionRepository.getSubscription(subscriptionId);

        validateState(subscription);
        subscriptionService.changeSubscriptionStart(subscription);

        ChangeOrderIsUsedResponse changeOrderIsUsedResponse = new ChangeOrderIsUsedResponse("주문", "이용권 사용 시작 처리하였습니다.");
        return new ResponseEntity(changeOrderIsUsedResponse, HttpStatus.ACCEPTED);
    }

    @PatchMapping
    public HttpEntity<SubscriptionChangeResponse> changeSubscription(@SessionAttribute("loginMember") Long memberId, @RequestBody @Validated SubscriptionChangeRequest subscriptionChangeRequest) {

        Member member = memberRepository.getMember(memberId);
        Subscription beforeSubscription = subscriptionRepository.getRepresentativeSubscription(member);
        Subscription afterSubscription = subscriptionRepository.getSubscription(subscriptionChangeRequest.getAfterSubscriptionId());

        validateChangeability(member, afterSubscription);
        subscriptionService.changeSubscription(beforeSubscription, afterSubscription);

        SubscriptionChangeResponse subscriptionChangeResponse = new SubscriptionChangeResponse("이용권", "이용권 변경에 성공하였습니다.");
        return new ResponseEntity(subscriptionChangeResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping
    public HttpEntity<List<SubscriptionListResponse>> showSubscriptionList(@SessionAttribute("loginMember") Long memberId) {

        Member member = memberRepository.getMember(memberId);
        List<Subscription> subscriptions = subscriptionRepository.getSubscriptions(member);

        List<SubscriptionListResponse> subscriptionListResponses = changeAllSubscriptionToSubscriptionListResponse(subscriptions);
        if (subscriptionListResponses.isEmpty()) { // 갖고 있는 이용권이 없는 경우
            throw new NotExistSubscriptionException("이용권이 존재하지 않습니다.");
        }

        return new ResponseEntity(subscriptionListResponses, HttpStatus.OK);
    }

    @GetMapping("/representative")
    public HttpEntity<RepresentativeSubscriptionResponse> showRepresentativeSubscription(@SessionAttribute("loginMember") Long memberId) {

        try {
            Member member = memberRepository.getMember(memberId);
            Subscription subscription = subscriptionRepository.getRepresentativeSubscription(member);
            return new ResponseEntity(changeSubscriptionToRepresentativeSubscriptionDto(subscription), HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistSubscriptionException("보유 중인 이용권이 존재하지 않습니다.");
        }
    }

    private void validateState(Subscription subscription) {

        if (subscription.getStartDateTime() != null) {
            throw new IllegalStateException("이미 사용 시작된 이용권입니다.");
        }
    }

    private void validateChangeability(Member member, Subscription afterSubscription) {

        // afterSubscription에 대한 검증
        validateExistSubscription(afterSubscription);
        validateMySubscription(member, afterSubscription);
        validateUsableSubscription(afterSubscription);
    }

    private void validateExistSubscription(Subscription subscription) {

        if (subscription == null) {
            throw new NotExistSubscriptionException("존재하지 않는 이용권입니다.");
        }
    }

    private void validateMySubscription(Member member, Subscription subscription) {

        if (!member.equals(subscription.getOrder().getMember())) {
            throw new SubscriptionChangeException("나의 이용권이 아닙니다.");
        }
    }

    private void validateUsableSubscription(Subscription subscription) {

        if (!subscription.isValid()) {
            throw new SubscriptionChangeException("유효하지 않은 이용권입니다.");
        }
    }

    private List<SubscriptionListResponse> changeAllSubscriptionToSubscriptionListResponse(List<Subscription> subscriptions) {

        List<SubscriptionListResponse> subscriptionListResponses = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            subscriptionListResponses.add(changeSubscriptionToSubscriptionListResponse(subscription));
        }
        return subscriptionListResponses;
    }

    private SubscriptionListResponse changeSubscriptionToSubscriptionListResponse(Subscription subscription) {

        return new SubscriptionListResponse(
                subscription.getId(),
                subscription.getOrder().getItem().getItemName(),
                subscription.isRepresentative(),
                subscription.getStartDateTime(),
                subscription.getEndDateTime(),
                getFormattedLeftTime(subscription.getLeftTime()));
    }

    private RepresentativeSubscriptionResponse changeSubscriptionToRepresentativeSubscriptionDto(Subscription subscription) {
        Item item = subscription.getOrder().getItem();
        return new RepresentativeSubscriptionResponse(item.getItemName(), gerFormattedEndDateTime(item.getItemType(), subscription.getEndDateTime(), subscription.getLeftTime()));
    }

    private String gerFormattedEndDateTime(ItemType itemType, LocalDateTime endDateTime, Duration leftTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초까지");

        String result = endDateTime.format(formatter);
        if (itemType == ItemType.CHARGE) {
            result += " (" + getFormattedLeftTime(leftTime) + " 남음)";
        }
        return result;
    }

    private String getFormattedLeftTime(Duration leftTime) {

        long seconds = leftTime.getSeconds();

        long hour = (int)(seconds / 3600);
        long minute = (int) ((seconds % 3600) / 60);
        long second = seconds % 60;

        return hour + "시간 " + minute + "분 " + second + "초";
    }
}
