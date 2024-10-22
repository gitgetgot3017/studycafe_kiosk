package lhj.studycafe_kiosk.subscription.exception;

import lhj.studycafe_kiosk.domain.ItemType;
import lhj.studycafe_kiosk.domain.Order;
import lhj.studycafe_kiosk.domain.Subscription;
import lhj.studycafe_kiosk.order.dto.ChangeOrderIsUsedResponse;
import lhj.studycafe_kiosk.subscription.SubscriptionRepository;
import lhj.studycafe_kiosk.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionRepository subscriptionRepository;

    @PatchMapping("/{subscriptionId}")
    public HttpEntity<ChangeOrderIsUsedResponse> startUseRequest(@PathVariable("subscriptionId") Long subscriptionId) {

        Subscription subscription = subscriptionRepository.getSubscription(subscriptionId);

        validateState(subscription);
        subscriptionService.changeSubscriptionStart(subscription);

        ChangeOrderIsUsedResponse changeOrderIsUsedResponse = new ChangeOrderIsUsedResponse("주문", "이용권 사용 시작 처리하였습니다.");
        return new ResponseEntity(changeOrderIsUsedResponse, HttpStatus.ACCEPTED);
    }

    private void validateState(Subscription subscription) {

        if (subscription.getStartDateTime() != null) {
            throw new IllegalStateException("이미 사용 시작된 이용권입니다.");
        }
    }
}
