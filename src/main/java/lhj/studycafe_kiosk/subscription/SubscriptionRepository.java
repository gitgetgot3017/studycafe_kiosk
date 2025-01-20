package lhj.studycafe_kiosk.subscription;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Order;
import lhj.studycafe_kiosk.domain.Subscription;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubscriptionRepository {

    @PersistenceContext
    EntityManager em;

    public Subscription getSubscription(Member member) {
        return em.createQuery("select s from Subscription s where s.member = :member", Subscription.class)
                .setParameter("member", member)
                .getSingleResult();
    }

    public void saveSubscription(Subscription subscription) {
        em.persist(subscription);
    }
}
