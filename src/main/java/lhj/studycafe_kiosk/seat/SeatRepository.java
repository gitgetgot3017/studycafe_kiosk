package lhj.studycafe_kiosk.seat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeatRepository {

    @PersistenceContext
    EntityManager em;

    public Seat getSeat(Long seatId) {
        return em.find(Seat.class, seatId);
    }

    public void updateTwoSeat(Seat beforeSeat, Seat afterSeat) {
        afterSeat.changeSeatState(beforeSeat.getMember(), beforeSeat.getEndDateTime());
        beforeSeat.changeSeatState(null, null);
    }

    public void updateSeat(Seat seat) {
        seat.changeSeatState(null, null);
    }

    public List<Seat> getOccupiedSeats() {
        return em.createQuery("select s from Seat s where s.member is not null")
                .getResultList();
    }

    public Seat getMySeat(Member member) {
        return em.createQuery("select s from Seat s where s.member = :member", Seat.class)
                .setParameter("member", member)
                .getSingleResult();
    }
}
