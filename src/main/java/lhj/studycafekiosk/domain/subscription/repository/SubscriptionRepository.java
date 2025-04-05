package lhj.studycafekiosk.domain.subscription.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.subscription.domain.Subscription;
import org.springframework.stereotype.Repository;

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
