package lhj.studycafekiosk.domain.reservation.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.reservation.domain.Reservation;
import lhj.studycafekiosk.domain.seat.domain.Seat;
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

    public Reservation getAlreadyReserved(Member member) {
        return em.createQuery("select r from Reservation r where r.finished = false and r.member = :member", Reservation.class)
                .setParameter("member", member)
                .getSingleResult();
    }
}
