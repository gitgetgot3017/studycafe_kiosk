package lhj.studycafe_kiosk.seat;

import lhj.studycafe_kiosk.domain.*;
import lhj.studycafe_kiosk.subscription.SubscriptionRepository;
import lhj.studycafe_kiosk.subscription.exception.ExpiredSubscriptionException;
import lhj.studycafe_kiosk.subscription.exception.NotExistSubscriptionException;
import lhj.studycafe_kiosk.usage_status.UsageStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService {

    private final SubscriptionRepository subscriptionRepository;
    private final SeatRepository seatRepository;
    private final UsageStatusRepository usageStatusRepository;

    public void chooseSeat(Member member, Seat seat) {

        Subscription subscription = getSubscription(member);
        Item item = subscription.getItem();

        LocalDateTime endDateTime;
        LocalDateTime curDateTime = LocalDateTime.now();
        Duration leftTime = subscription.getLeftTime();

        if (curDateTime.isAfter(subscription.getEndDateTime())) {
            throw new ExpiredSubscriptionException("이용 기간이 만료되었습니다.");
        }

        if (item.getItemType() == ItemType.DAILY) {
            endDateTime = getEndDateTime(curDateTime, leftTime);
        } else if (item.getItemType() == ItemType.CHARGE || item.getItemType() == ItemType.PERIOD) {
            if (leftTime.getSeconds() < 24*60*60) {
                endDateTime = getEndDateTime(curDateTime, leftTime);
            } else {
                endDateTime = LocalDateTime.now().plusHours(24);
            }
        } else {
            endDateTime = subscription.getEndDateTime();
        }

        seat.changeSeatState(member, endDateTime);
        usageStatusRepository.saveUsageStatus(new UsageStatus(subscription, UserInOut.IN, curDateTime));
    }

    public void changeSeat(Seat beforeSeat, Seat afterSeat) {

        seatRepository.updateSeat(beforeSeat, afterSeat);
    }

    private Subscription getSubscription(Member member) {
        try {
            return subscriptionRepository.getRepresentativeSubscription(member);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistSubscriptionException("이용권이 존재하지 않습니다.");
        }
    }

    private LocalDateTime getEndDateTime(LocalDateTime dateTime, Duration duration) {
        return dateTime.plus(duration);
    }
}
