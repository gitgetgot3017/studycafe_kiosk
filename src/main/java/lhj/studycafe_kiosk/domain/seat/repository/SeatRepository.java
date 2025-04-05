package lhj.studycafe_kiosk.domain.seat.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.member.domain.Member;
import lhj.studycafe_kiosk.ScheduledTask;
import lhj.studycafe_kiosk.domain.seat.domain.Seat;
import lhj.studycafe_kiosk.TaskType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class SeatRepository {

    @PersistenceContext
    EntityManager em;

    public Seat getSeat(Long seatId) {
        return em.find(Seat.class, seatId);
    }

    public void updateTwoSeat(Seat beforeSeat, Seat afterSeat) {
        afterSeat.chooseSeat(beforeSeat.getMember());
        beforeSeat.chooseSeat(null);
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

    @Transactional // TODO: 근본적인 해결 방법을 찾아야 함
    public void vacateSeat(Member member) {
        em.createQuery("update Seat s set s.member = null where s.member = :member")
                .setParameter("member", member)
                .executeUpdate();
    }

    public void saveScheduledTask(ScheduledTask scheduledTask) {
        em.persist(scheduledTask);
    }

    public List<ScheduledTask> getAllScheduledTask() {
        return em.createQuery("select s from ScheduledTask s", ScheduledTask.class)
                .getResultList();
    }

    public void deleteScheduledTask(Long scheduledTaskId) {
        ScheduledTask scheduledTask = em.find(ScheduledTask.class, scheduledTaskId);
        if (scheduledTask != null) {
           em.remove(scheduledTask);
       }
    }

    public void deleteScheduledTaskByMemberAndType(Member member, TaskType type) {
        em.createQuery("delete from ScheduledTask s where s.member = :member and s.type = :type")
                .setParameter("member", member)
                .setParameter("type", type)
                .executeUpdate();
    }
}
