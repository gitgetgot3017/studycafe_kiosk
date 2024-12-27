package lhj.studycafe_kiosk.reservation;

import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Seat;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.reservation.dto.ReservationRequest;
import lhj.studycafe_kiosk.reservation.exception.AlreadyReservedSeatException;
import lhj.studycafe_kiosk.reservation.exception.ReservationNotPossibleException;
import lhj.studycafe_kiosk.seat.SeatRepository;
import lhj.studycafe_kiosk.seat.exception.EmptySeatOutException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final SeatRepository seatRepository;

    @PostMapping
    public void makeReservation(@SessionAttribute("loginMember") Long memberId, ReservationRequest reservationRequest) {

        Member member = memberRepository.getMember(memberId);
        Seat seat = seatRepository.getSeat(reservationRequest.getSeatId());

        validateBookableSeat(seat, member);

        reservationService.makeReservation(member, seat);
    }

    private void validateBookableSeat(Seat seat, Member member) {

        // 빈 좌석에 대해 예약을 시도하는 경우
        if (seat.getMember() == null) {
            throw new EmptySeatOutException("빈 좌석입니다. 예약 없이 바로 이용 가능합니다.");
        }

        // 이미 예약된 좌석에 대해 예약을 시도하는 경우
        try {
            reservationRepository.getAlreadyReservedSeat(seat);
        } catch (EmptyResultDataAccessException e) {
            throw new AlreadyReservedSeatException("이미 예약된 좌석입니다.");
        }

        // 내가 사용 중인 좌석에 대해 예약을 시도하는 경우
        if (seat.getMember() == member) {
            throw new ReservationNotPossibleException("나의 좌석에 대해서는 예약할 수 없습니다.");
        }
    }
}
