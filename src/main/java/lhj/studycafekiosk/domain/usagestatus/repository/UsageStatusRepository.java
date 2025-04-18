package lhj.studycafekiosk.domain.usagestatus.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.usagestatus.domain.UsageStatus;
import lhj.studycafekiosk.domain.main.domain.UserInOut;
import org.springframework.stereotype.Repository;

@Repository
public class UsageStatusRepository {

    @PersistenceContext
    EntityManager em;

    public void saveUsageStatus(UsageStatus usageStatus) {
        em.persist(usageStatus);
    }

    public UsageStatus getUsageStatusIn(Member member) {
        return em.createQuery("select u from UsageStatus u where u.member = :member and u.userInOut = :userInOut order by u.userDateTime desc", UsageStatus.class)
                .setParameter("member", member)
                .setParameter("userInOut", UserInOut.IN)
                .setFirstResult(0)
                .getSingleResult();
    }

    public UsageStatus getUsageStatus(Member member) {
        return em.createQuery("select u from UsageStatus  u where u.member = :member order by u.userDateTime desc", UsageStatus.class)
                .setParameter("member", member)
                .setFirstResult(0)
                .setMaxResults(1)
                .getSingleResult();
    }
}
