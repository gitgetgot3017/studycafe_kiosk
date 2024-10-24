package lhj.studycafe_kiosk.subscription;

import lhj.studycafe_kiosk.domain.Item;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Order;
import lhj.studycafe_kiosk.domain.Subscription;
import lhj.studycafe_kiosk.subscription.exception.NotExistSubscriptionException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public void issueSubscription(Member member, Item item, Order order) {

        boolean isRepresentative = false;
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        try {
            subscriptionRepository.getRepresentativeSubscription(member);
        } catch (EmptyResultDataAccessException e) {
            isRepresentative = true;
            startDateTime = LocalDateTime.now();
            endDateTime = LocalDateTime.now().plusSeconds(item.getDuration().getSeconds());
        }

        Subscription subscription = new Subscription(order, isRepresentative, startDateTime, endDateTime, item.getDuration(), true);
        subscriptionRepository.saveSubscription(subscription);
    }

    public void changeSubscriptionInvalid(Subscription subscription) {

        subscription.setSubscriptionInvalid();
    }

    public void changeSubscriptionStart(Subscription subscription) {
        subscription.startSubscription();
    }
}
