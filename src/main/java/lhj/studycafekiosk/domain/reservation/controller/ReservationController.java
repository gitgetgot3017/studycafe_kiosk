package lhj.studycafekiosk.domain.reservation.controller;

import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.reservation.repository.ReservationRepository;
import lhj.studycafekiosk.domain.reservation.repository.ReservationService;
import lhj.studycafekiosk.domain.seat.domain.Seat;
import lhj.studycafekiosk.domain.member.repository.MemberRepository;
import lhj.studycafekiosk.domain.reservation.dto.DuplicateReservationException;
import lhj.studycafekiosk.domain.reservation.dto.ReservationRequest;
import lhj.studycafekiosk.domain.reservation.exception.AlreadyReservedSeatException;
import lhj.studycafekiosk.domain.reservation.exception.ReservationNotPossibleException;
import lhj.studycafekiosk.domain.seat.repository.SeatRepository;
import lhj.studycafekiosk.domain.seat.exception.EmptySeatOutException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final SeatRepository seatRepository;

    @PostMapping
//    public void makeReservation(@SessionAttribute("loginMember") Long memberId, ReservationRequest reservationRequest) {
    public void makeReservation(@RequestBody @Validated ReservationRequest reservationRequest) {

        Member member = memberRepository.getMember(3L);
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
            throw new AlreadyReservedSeatException("이미 예약된 좌석입니다.");
        } catch (EmptyResultDataAccessException e) {
            // 예약된 좌석이 아닌 경우
        }

        // 내가 사용 중인 좌석에 대해 예약을 시도하는 경우
        if (seat.getMember() == member) {
            throw new ReservationNotPossibleException("나의 좌석에 대해서는 예약할 수 없습니다.");
        }

        // 두 좌석 이상에 대해 예약을 시도하는 경우
        try {
            reservationRepository.getAlreadyReserved(member);
            throw new DuplicateReservationException("하나의 좌석에 대해서만 예약 가능합니다.");
        } catch (EmptyResultDataAccessException e) {
            // 한 사람이 하나 이하의 좌석을 예약한 경우
        }
    }
}
