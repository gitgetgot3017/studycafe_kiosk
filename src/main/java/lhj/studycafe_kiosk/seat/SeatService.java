package lhj.studycafe_kiosk.seat;

import lhj.studycafe_kiosk.domain.*;
import lhj.studycafe_kiosk.subscription.SubscriptionRepository;
import lhj.studycafe_kiosk.subscription.SubscriptionService;
import lhj.studycafe_kiosk.subscription.exception.ExpiredSubscriptionException;
import lhj.studycafe_kiosk.subscription.exception.NotExistSubscriptionException;
import lhj.studycafe_kiosk.usage_status.UsageStatusRepository;
import lhj.studycafe_kiosk.usage_status.UsageStatusService;
import lhj.studycafe_kiosk.usage_status.exception.UserNotInException;
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

    private final SubscriptionService subscriptionService;
    private final SubscriptionRepository subscriptionRepository;
    private final SeatRepository seatRepository;
    private final UsageStatusService usageStatusService;
    private final UsageStatusRepository usageStatusRepository;

    public void chooseSeat(Member member, Seat seat) {

        Subscription subscription = getSubscription(member);
        Item item = subscription.getOrder().getItem();

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
    }

    public void changeSeat(Seat beforeSeat, Seat afterSeat) {

        seatRepository.updateTwoSeat(beforeSeat, afterSeat);
    }

    public Duration outSeat(Seat seat, Member member) {

        // 이용한 좌석 비우기
        seatRepository.updateSeat(seat);

        // 좌석 퇴실에 대한 UsageStatus 남기기
        Subscription subscription = getSubscription(member);
        usageStatusService.recordUsageStatus(new UsageStatus(subscription, member, UserInOut.OUT, LocalDateTime.now()));

        // 남은 시간 보여주기
        Item item = subscription.getOrder().getItem();
        Duration remainderTime;

        if (item.getItemType() == ItemType.DAILY) {
            remainderTime = Duration.ZERO;
        } else if (item.getItemType() == ItemType.CHARGE) {
            remainderTime = subscription.getLeftTime().minus(getUsageTime(member));
        } else {
            remainderTime = Duration.between(LocalDateTime.now(), subscription.getEndDateTime());
        }

        // 이용권 남은 시간 갱신, 남은 시간이 0이 되면 해당 이용권 삭제
        subscription.setLeftTime(remainderTime);

        if (remainderTime == Duration.ZERO || remainderTime.isNegative()) {
            subscriptionService.changeSubscriptionInvalid(subscription);
        }

        return remainderTime;
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

    private Duration getUsageTime(Member member) {
        Duration usageTime;
        try {
            UsageStatus usageStatus = usageStatusRepository.getUsageStatusIn(member);
            usageTime = Duration.between(LocalDateTime.now(), usageStatus.getUserDateTime());
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotInException("입실 처리를 하지 않았습니다.");
        }
        return usageTime;
    }

    public void vacateSeat(Member member) {
        seatRepository.vacateSeat(member);
    }

    public void registerScheduledTask(ScheduledTask scheduledTask) {
        seatRepository.saveScheduledTask(scheduledTask);
    }
}
