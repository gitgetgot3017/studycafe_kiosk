package lhj.studycafe_kiosk.seat;

import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Seat;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.seat.dto.SeatResponse;
import lhj.studycafe_kiosk.seat.exception.NotExistSeatException;
import lhj.studycafe_kiosk.seat.exception.NotUsableSeatException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/{seatId}")
    public HttpEntity<SeatResponse> chooseSeat(@PathVariable("seatId") Long seatId) {

        Seat seat = seatRepository.getSeat(seatId);
        Member member = memberRepository.getMember(30L);

        validateUsableSeat(seat);
        seatService.chooseSeat(member, seat);

        SeatResponse seatResponse = new SeatResponse("좌석 선택이 완료되었습니다.", seatId);
        return new ResponseEntity(seatResponse, HttpStatus.ACCEPTED);
    }

    private void validateUsableSeat(Seat seat) {

        if (seat == null) {
            throw new NotExistSeatException("존재하지 않는 좌석 번호입니다.");
        }

        if (seat.getMember() != null) {
            throw new NotUsableSeatException("이미 사용 중인 좌석입니다.");
        }
    }
}
