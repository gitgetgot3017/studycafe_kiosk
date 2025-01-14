package lhj.studycafe_kiosk.seat;

import lhj.studycafe_kiosk.domain.*;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.seat.dto.*;
import lhj.studycafe_kiosk.seat.exception.EmptySeatOutException;
import lhj.studycafe_kiosk.seat.exception.InvalidSeatChangeException;
import lhj.studycafe_kiosk.seat.exception.NotExistSeatException;
import lhj.studycafe_kiosk.seat.exception.NotUsableSeatException;
import lhj.studycafe_kiosk.subscription.SubscriptionRepository;
import lhj.studycafe_kiosk.usage_status.UsageStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import static lhj.studycafe_kiosk.domain.TaskType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UsageStatusRepository usageStatusRepository;
    private final TaskScheduler taskScheduler;

    private Map<Long, ScheduledFuture<?>> taskReservations = new ConcurrentHashMap<>();

    @PostMapping("/{seatId}")
    public HttpEntity<SeatResponse> chooseSeat(@SessionAttribute("loginMember") Long memberId, @RequestParam(value = "seatId", defaultValue = "/") Long seatId) {

        Seat seat = seatRepository.getSeat(seatId);
        Member member = memberRepository.getMember(memberId);
        Subscription subscription = subscriptionRepository.getRepresentativeSubscription(member);

        validateUsableSeat(seat);
        seatService.chooseSeat(member, seat);

        // 기간권의 경우 좌석 선택 후 10분 내에 입실하지 않으면 vacate 처리됨
        ItemType itemType = subscription.getOrder().getItem().getItemType();
        if (itemType == ItemType.PERIOD) {
            Instant executionTime = Instant.now().plus(10, ChronoUnit.MINUTES);
            taskScheduler.schedule(new EnterCheckTask(member, usageStatusRepository, seatService), executionTime);
            registerScheduledTask(member, LocalDateTime.ofInstant(executionTime, ZoneId.of("Asia/Seoul")), ENTRACELIMIT);
        }

        // (모든 이용권에 대해) 좌석을 vacate하는 작업 등록하기
        registerVacateSeat(subscription.getLeftTime(), itemType, member);

        SeatResponse seatResponse = new SeatResponse("좌석 선택이 완료되었습니다.", seatId);
        return new ResponseEntity(seatResponse, HttpStatus.ACCEPTED);
    }

    private void registerVacateSeat(Duration leftTime, ItemType itemType, Member member) {

        long seconds = leftTime.getSeconds();
        if (itemType == ItemType.CHARGE || itemType == ItemType.PERIOD) {
            if (seconds > 24 * 60 * 60) {
                seconds = 24 * 60 * 60;
            }
        }

        Instant executionTime = Instant.now().plus(seconds, ChronoUnit.SECONDS);
        ScheduledFuture<?> taskReservation = taskScheduler.schedule(new VacateSeatTask(member, seatService), executionTime);
        taskReservations.put(member.getId(), taskReservation);
        registerScheduledTask(member, LocalDateTime.ofInstant(executionTime, ZoneId.of("Asia/Seoul")), VACATESEAT);
    }

    private void registerScheduledTask(Member member, LocalDateTime executionTime, TaskType type) {
        seatService.registerScheduledTask(new ScheduledTask(member, executionTime, type));
    }

    @PatchMapping
    public HttpEntity<SeatChangeSuccessResponse> changeSeat(@SessionAttribute("loginMember") Long memberId, @RequestBody @Validated SeatChangeRequest seatChangeRequest) {

        Member member = memberRepository.getMember(memberId);
        Seat beforeSeat = seatRepository.getMySeat(member);
        Seat afterSeat = seatRepository.getSeat(seatChangeRequest.getAfterSeatId());

        validateChangeability(member, beforeSeat, afterSeat);
        seatService.changeSeat(beforeSeat, afterSeat);

        SeatChangeSuccessResponse seatChangeSuccessResponse = new SeatChangeSuccessResponse("좌석 이동을 완료하였습니다.", beforeSeat.getId(), seatChangeRequest.getAfterSeatId());
        return new ResponseEntity(seatChangeSuccessResponse, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{seatId}")
    public HttpEntity<UserOutSeatResponse> outSeat(@SessionAttribute("loginMember") Long memberId, @PathVariable("seatId") Long seatId) {

        Seat seat = seatRepository.getSeat(seatId);
        Member member = memberRepository.getMember(memberId);

        validateIsMySeat(member, seat);
        Duration remainderTime = seatService.outSeat(seat, member);

        UserOutSeatResponse outSeatResponse = new UserOutSeatResponse("좌석 퇴실이 완료되었습니다.", seatId, changeDurationToString(remainderTime));
        return new ResponseEntity<>(outSeatResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping("/occupied")
    public HttpEntity<List<OccupiedSeatResponse>> showSeats(@SessionAttribute(value = "loginMember", required = false) Long memberId) {

        List<Seat> unusableSeats = seatRepository.getOccupiedSeats();

        List<OccupiedSeatResponse> occupiedSeats = getOccupiedSeats(unusableSeats, memberId);
        return new ResponseEntity<>(occupiedSeats, HttpStatus.OK);
    }

    @GetMapping("/me")
    public HttpEntity<MySeatResponse> getMySeat(@SessionAttribute("loginMember") Long memberId) {

        Member member = memberRepository.getMember(memberId);
        Seat mySeat = seatRepository.getMySeat(member);

        MySeatResponse mySeatResponse = new MySeatResponse(mySeat.getId(), getEntranceCode(member));
        return new ResponseEntity<>(mySeatResponse, HttpStatus.OK);
    }

    private void validateUsableSeat(Seat seat) {

        if (seat == null) {
            throw new NotExistSeatException("존재하지 않는 좌석 번호입니다.");
        }

        if (seat.getMember() != null) {
            throw new NotUsableSeatException("이미 사용 중인 좌석입니다.");
        }
    }

    private void validateIsMySeat(Member member, Seat seat) {

        if (seat.getMember() == null) {
            throw new EmptySeatOutException("이미 빈 좌석입니다.");
        }

        if (!seat.getMember().equals(member)) {
            throw new InvalidSeatChangeException("현재 나의 좌석이 아닙니다.");
        }
    }

    private void validateChangeability(Member member, Seat beforeSeat, Seat afterSeat) {

        // 이동 가능한 좌석인지 검증
        validateUsableSeat(afterSeat);
    }

    private String changeDurationToString(Duration duration) {

        if (duration == Duration.ZERO || duration.isNegative()) {
            return "이용 기간이 만료되었습니다.";
        }

        long leftDay = duration.toDays();
        long leftHour = duration.toHours() % 24;
        long leftMinute = duration.toMinutes() % 60;
        return String.format("이용 기간이 %d일 %d시간 %d분 남았습니다.", leftDay, leftHour, leftMinute);
    }

    private List<OccupiedSeatResponse> getOccupiedSeats(List<Seat> seats, Long memberId) {

        List<OccupiedSeatResponse> occupiedSeats = new ArrayList();
        for (Seat seat : seats) {
            occupiedSeats.add(new OccupiedSeatResponse(seat.getId(), seat.getMember().getId() == memberId));
        }
        return occupiedSeats;
    }

    private String getEntranceCode(Member member) {

        String phone = member.getPhone();
        String password = member.getPassword();
        return phone.substring(phone.length() - 4) + " | " + password.substring(0, 2);
    }
}
