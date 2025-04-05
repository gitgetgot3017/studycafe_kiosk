package lhj.studycafe_kiosk.domain.seat.service;

import lhj.studycafe_kiosk.ScheduledTask;
import lhj.studycafe_kiosk.TaskType;
import lhj.studycafe_kiosk.domain.item.domain.Item;
import lhj.studycafe_kiosk.domain.item.domain.ItemType;
import lhj.studycafe_kiosk.domain.main.domain.UserInOut;
import lhj.studycafe_kiosk.domain.member.domain.Member;
import lhj.studycafe_kiosk.domain.member.repository.MemberRepository;
import lhj.studycafe_kiosk.domain.order.domain.Order;
import lhj.studycafe_kiosk.domain.order.repository.OrderRepository;
import lhj.studycafe_kiosk.domain.reservation.domain.Reservation;
import lhj.studycafe_kiosk.EnterCheckTask;
import lhj.studycafe_kiosk.VacateSeatTask;
import lhj.studycafe_kiosk.domain.seat.domain.Seat;
import lhj.studycafe_kiosk.domain.seat.exception.NotExistSeatException;
import lhj.studycafe_kiosk.domain.seat.exception.NotUsableSeatException;
import lhj.studycafe_kiosk.domain.seat.repository.SeatRepository;
import lhj.studycafe_kiosk.domain.subscription.domain.Subscription;
import lhj.studycafe_kiosk.domain.subscription.exception.ExpiredSubscriptionException;
import lhj.studycafe_kiosk.domain.subscription.exception.NotExistSubscriptionException;
import lhj.studycafe_kiosk.domain.subscription.repository.SubscriptionRepository;
import lhj.studycafe_kiosk.domain.subscription.service.SubscriptionService;
import lhj.studycafe_kiosk.domain.usage_status.domain.UsageStatus;
import lhj.studycafe_kiosk.domain.notification.service.NotificationService;
import lhj.studycafe_kiosk.domain.reservation.repository.ReservationRepository;
import lhj.studycafe_kiosk.domain.usage_status.repository.UsageStatusRepository;
import lhj.studycafe_kiosk.domain.usage_status.service.UsageStatusService;
import lhj.studycafe_kiosk.domain.usage_status.exception.UserNotInException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import static lhj.studycafe_kiosk.TaskType.ENTRACELIMIT;
import static lhj.studycafe_kiosk.TaskType.VACATESEAT;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService {

    private final MemberRepository memberRepository;
    private final SubscriptionService subscriptionService;
    private final SubscriptionRepository subscriptionRepository;
    private final SeatRepository seatRepository;
    private final UsageStatusService usageStatusService;
    private final UsageStatusRepository usageStatusRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;
    private final TaskScheduler taskScheduler;

    private Map<Long, ScheduledFuture<?>> enterTaskReservations = new ConcurrentHashMap<>();
    private Map<Long, ScheduledFuture<?>> vacateSeatTaskReservations = new ConcurrentHashMap<>();

    // 좌석 선택을 하기 위해서는 (1) 로그인이 되어 있어야 하고 (2) 유효한 이용권이 존재해야 하고 (3) 유효한 좌석을 선택해야 한다.
    public void chooseSeat(Long memberId, Long seatId) {

        Seat seat = seatRepository.getSeat(seatId);
        Member member = memberRepository.getMember(memberId);

        Subscription subscription = validateUsableSubscription(member);
        validateUsableSeat(seat);

        seat.chooseSeat(member);

        // 기간권의 경우 좌석 선택 후 10분 내에 입실하지 않으면 vacate 처리됨
        Order order = getOrder(member);
        ItemType itemType = order.getItem().getItemType();
        if (itemType == ItemType.PERIOD) {
            Instant executionTime = Instant.now().plus(10, ChronoUnit.MINUTES);
            ScheduledFuture<?> taskReservation = taskScheduler.schedule(new EnterCheckTask(member, usageStatusRepository, this), executionTime);
            enterTaskReservations.put(seatId, taskReservation);
            registerScheduledTask(member, LocalDateTime.ofInstant(executionTime, ZoneId.of("Asia/Seoul")), ENTRACELIMIT);
        }

        // (모든 이용권에 대해) 좌석을 vacate하는 작업 등록하기
        registerVacateSeat(subscription.getLeftTime(), itemType, member);
    }

    private Subscription validateUsableSubscription(Member member) {

        try {
            Subscription subscription = subscriptionRepository.getSubscription(member);
            if (LocalDateTime.now().isAfter(subscription.getEndDateTime())) {
                throw new ExpiredSubscriptionException("이용 기간이 만료되었습니다.");
            }
            return subscription;
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistSubscriptionException("보유 중인 이용권이 존재하지 않습니다.");
        }
    }

    private void validateUsableSeat(Seat seat) {

        if (seat == null) {
            throw new NotExistSeatException("존재하지 않는 좌석 번호입니다.");
        }

        if (seat.getMember() != null) {
            throw new NotUsableSeatException("이미 사용 중인 좌석입니다.");
        }
    }

    private Order getOrder(Member member) {

        try {
            return orderRepository.getItemByMember(member);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistSubscriptionException("구매 내역이 존재하지 않습니다.");
        }
    }

    private void registerVacateSeat(Duration leftTime, ItemType itemType, Member member) {

        long seconds = leftTime.getSeconds();
        if (itemType == ItemType.CHARGE || itemType == ItemType.PERIOD) {
            if (seconds > 24 * 60 * 60) {
                seconds = 24 * 60 * 60;
            }
        }

        Instant executionTime = Instant.now().plus(seconds, ChronoUnit.SECONDS);
        ScheduledFuture<?> taskReservation = taskScheduler.schedule(new VacateSeatTask(member, this), executionTime);
        vacateSeatTaskReservations.put(member.getId(), taskReservation);
        registerScheduledTask(member, LocalDateTime.ofInstant(executionTime, ZoneId.of("Asia/Seoul")), VACATESEAT);
    }

    private void registerScheduledTask(Member member, LocalDateTime executionTime, TaskType type) {
        registerScheduledTask(new ScheduledTask(member, executionTime, type));
    }

    public void changeSeat(Seat beforeSeat, Seat afterSeat) {

        seatRepository.updateTwoSeat(beforeSeat, afterSeat);
    }

    public Duration outSeat(Long seatId, Member member) {

        // [메인] 좌석을 비운다.
        seatRepository.vacateSeat(member);

        // 해당 좌석에 대해 10분 내 입실 확인 예약이 등록되어 있으면 취소한다.
        if (enterTaskReservations.containsKey(seatId)) {
            enterTaskReservations.get(seatId).cancel(true);
            seatRepository.deleteScheduledTaskByMemberAndType(member, ENTRACELIMIT);
        }

        // 좌석 퇴실에 대한 UsageStatus 남기기
        Subscription subscription = getSubscription(member);
        usageStatusService.recordUsageStatus(new UsageStatus(subscription, member, UserInOut.OUT, LocalDateTime.now()));

        // 남은 시간 보여주기
        Order order = orderRepository.getItemByMember(member);
        Item item = order.getItem();
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

        // 해당 좌석이 예약된 좌석이라면 예약에 대한 처리를 해준다.
        try {
            Seat seat = seatRepository.getSeat(seatId);
            Reservation reservation = reservationRepository.getAlreadyReservedSeat(seat);
            notificationService.send(reservation.getMember().getId()); // 예약한 회원에게 알림을 준다.
        } catch (EmptyResultDataAccessException e) {
            // 예약된 좌석이 아닌 경우
        }

        return remainderTime;
    }

    private Subscription getSubscription(Member member) {
        try {
            return subscriptionRepository.getSubscription(member);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistSubscriptionException("이용권이 존재하지 않습니다.");
        }
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

    public void removeScheduledTask(ScheduledTask scheduledTask) {
        seatRepository.deleteScheduledTask(scheduledTask.getId());
    }
}
