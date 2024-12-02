package lhj.studycafe_kiosk.seat;

import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Seat;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.seat.dto.*;
import lhj.studycafe_kiosk.seat.exception.EmptySeatOutException;
import lhj.studycafe_kiosk.seat.exception.InvalidSeatChangeException;
import lhj.studycafe_kiosk.seat.exception.NotExistSeatException;
import lhj.studycafe_kiosk.seat.exception.NotUsableSeatException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/{seatId}")
    public HttpEntity<SeatResponse> chooseSeat(@SessionAttribute("loginMember") Long memberId, @PathVariable("seatId") Long seatId) {

        Seat seat = seatRepository.getSeat(seatId);
        Member member = memberRepository.getMember(memberId);

        validateUsableSeat(seat);
        seatService.chooseSeat(member, seat);

        SeatResponse seatResponse = new SeatResponse("좌석 선택이 완료되었습니다.", seatId);
        return new ResponseEntity(seatResponse, HttpStatus.ACCEPTED);
    }

    @PatchMapping
    public HttpEntity<SeatChangeSuccessResponse> changeSeat(@SessionAttribute("loginMember") Long memberId, @RequestBody @Validated SeatChangeRequest seatChangeRequest) {

        Seat beforeSeat = seatRepository.getSeat(seatChangeRequest.getBeforeSeatId());
        Seat afterSeat = seatRepository.getSeat(seatChangeRequest.getAfterSeatId());
        Member member = memberRepository.getMember(memberId);

        validateChangeability(member, beforeSeat, afterSeat);
        seatService.changeSeat(beforeSeat, afterSeat);

        SeatChangeSuccessResponse seatChangeSuccessResponse = new SeatChangeSuccessResponse("좌석 이동을 완료하였습니다.", seatChangeRequest.getBeforeSeatId(), seatChangeRequest.getAfterSeatId());
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

    private void validateIsMySeat(Member member, Seat beforeSeat) {

        if (beforeSeat.getMember() == null) {
            throw new EmptySeatOutException("이미 빈 좌석입니다.");
        }

        if (!beforeSeat.getMember().equals(member)) {
            throw new InvalidSeatChangeException("현재 나의 좌석이 아닙니다.");
        }
    }

    private void validateChangeability(Member member, Seat beforeSeat, Seat afterSeat) {

        // 현재 나의 좌석인지 검증
        validateIsMySeat(member, beforeSeat);

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
