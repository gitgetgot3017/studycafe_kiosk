package lhj.studycafe_kiosk.subscription;

import lhj.studycafe_kiosk.domain.*;
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

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        try {
            subscriptionRepository.getSubscription(member);
        } catch (EmptyResultDataAccessException e) {
            startDateTime = LocalDateTime.now();
            endDateTime = getEndDateTime(startDateTime, item);
        }

        Subscription subscription = new Subscription(order, startDateTime, endDateTime, getLeftTime(item), true);
        subscriptionRepository.saveSubscription(subscription);
    }

    private LocalDateTime getEndDateTime(LocalDateTime startDateTime, Item item) {

        if (item.getItemType() == ItemType.DAILY) {
            return startDateTime.plusHours(item.getUsageTime());
        } else {
            return startDateTime.plusDays(item.getUsagePeriod());
        }
    }

    private Duration getLeftTime(Item item) {

        if (item.getItemType() == ItemType.CHARGE) {
            return Duration.ofHours(item.getUsageTime());
        } else {
            return Duration.ZERO;
        }
    }

    public void changeSubscriptionInvalid(Subscription subscription) {

        subscription.setSubscriptionInvalid();
    }
}
