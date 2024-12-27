package lhj.studycafe_kiosk.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Reservation;
import lhj.studycafe_kiosk.domain.Seat;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationRepository {

    @PersistenceContext
    EntityManager em;

    public void saveReservation(Reservation reservation) {
        em.persist(reservation);
    }

    public Reservation getAlreadyReservedSeat(Seat seat) {
        return em.createQuery("select r from Reservation r where r.finished = false and r.seat = :seat", Reservation.class)
                .setParameter("seat", seat)
                .getSingleResult();
    }
}
