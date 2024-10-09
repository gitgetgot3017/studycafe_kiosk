package lhj.studycafe_kiosk.seat;

import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Seat;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.seat.dto.SeatChangeRequest;
import lhj.studycafe_kiosk.seat.dto.SeatChangeSuccessResponse;
import lhj.studycafe_kiosk.seat.dto.SeatResponse;
import lhj.studycafe_kiosk.seat.exception.InvalidSeatChangeException;
import lhj.studycafe_kiosk.seat.exception.NotExistSeatException;
import lhj.studycafe_kiosk.seat.exception.NotUsableSeatException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

        validateChangeability(memberId, beforeSeat, afterSeat);
        seatService.changeSeat(beforeSeat, afterSeat);

        SeatChangeSuccessResponse seatChangeSuccessResponse = new SeatChangeSuccessResponse("좌석 이동을 완료하였습니다.", seatChangeRequest.getBeforeSeatId(), seatChangeRequest.getAfterSeatId());
        return new ResponseEntity(seatChangeSuccessResponse, HttpStatus.ACCEPTED);
    }

    private void validateUsableSeat(Seat seat) {

        if (seat == null) {
            throw new NotExistSeatException("존재하지 않는 좌석 번호입니다.");
        }

        if (seat.getMember() != null) {
            throw new NotUsableSeatException("이미 사용 중인 좌석입니다.");
        }
    }

    private void validateChangeability(Long memberId, Seat beforeSeat, Seat afterSeat) {

        // 현재 나의 좌석인지 검증
        if (beforeSeat.getMember().getId() != memberId) {
            throw new InvalidSeatChangeException("현재 나의 좌석이 아닙니다.");
        }

        // 이동 가능한 좌석인지 검증
        validateUsableSeat(afterSeat);
    }
}
