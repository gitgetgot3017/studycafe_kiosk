package lhj.studycafe_kiosk.seat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Seat;
import org.springframework.stereotype.Repository;

@Repository
public class SeatRepository {

    @PersistenceContext
    EntityManager em;

    public Seat getSeat(Long seatId) {
        return em.find(Seat.class, seatId);
    }

    public void updateSeat(Seat beforeSeat, Seat afterSeat) {

        afterSeat.changeSeatState(beforeSeat.getMember(), beforeSeat.getEndDateTime());
        beforeSeat.changeSeatState(null, null);
    }
}
