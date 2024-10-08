package lhj.studycafe_kiosk.subscription;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Subscription;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionRepository {

    @PersistenceContext
    EntityManager em;

    public Subscription getRepresentativeSubscription(Member member) {
        Subscription subscription = em.createQuery("select s from Subscription s where s.member = :member and s.isRepresentative = :isRepresentative", Subscription.class)
                .setParameter("member", member)
                .setParameter("isRepresentative", true)
                .getSingleResult();
        return subscription;
    }
}
