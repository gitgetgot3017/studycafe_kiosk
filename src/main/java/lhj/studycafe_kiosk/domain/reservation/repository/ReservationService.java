package lhj.studycafe_kiosk.domain.reservation.repository;

import lhj.studycafe_kiosk.domain.member.domain.Member;
import lhj.studycafe_kiosk.domain.reservation.domain.Reservation;
import lhj.studycafe_kiosk.domain.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public void makeReservation(Member member, Seat seat) {

        Reservation reservation = new Reservation(member, seat, LocalDateTime.now(), false, false);
        reservationRepository.saveReservation(reservation);
    }
}
