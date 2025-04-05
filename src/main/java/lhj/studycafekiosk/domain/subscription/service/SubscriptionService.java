package lhj.studycafekiosk.domain.subscription.service;

import lhj.studycafekiosk.domain.item.domain.Item;
import lhj.studycafekiosk.domain.item.domain.ItemType;
import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.subscription.domain.Subscription;
import lhj.studycafekiosk.domain.subscription.repository.SubscriptionRepository;
import lhj.studycafekiosk.domain.order.service.OrderService;
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
    private final OrderService orderService;

    public void issueOrExtendSubscription(Member member, Item item) {

        try { // 이용권이 존재하는 유저인 경우: 이용권을 연장한다.
            Subscription subscription = subscriptionRepository.getSubscription(member);
            orderService.extendSubscription(item, subscription);
        } catch (EmptyResultDataAccessException e) { // 이용권이 존재하지 않는 유저인 경우: 이용권을 구매한다.
            LocalDateTime startDateTime = LocalDateTime.now();
            LocalDateTime endDateTime = getEndDateTime(startDateTime, item);
            Subscription subscription = new Subscription(member, startDateTime, endDateTime, getLeftTime(item), true);
            subscriptionRepository.saveSubscription(subscription);
        }
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
