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

    public Subscription getRepresentativeSubscription(Member member) {
        return em.createQuery("select s from Subscription s where s.order.member = :member and s.isRepresentative = :isRepresentative", Subscription.class)
                .setParameter("member", member)
                .setParameter("isRepresentative", true)
                .getSingleResult();
    }

    public void saveSubscription(Subscription subscription) {
        em.persist(subscription);
    }

    public Subscription getSubscriptionByOrder(Order order) {
        return em.createQuery("select s from Subscription s where s.order = :order", Subscription.class)
                .setParameter("order", order)
                .getSingleResult();
    }

    public Subscription getSubscription(Long subscriptionId) {
        return em.find(Subscription.class, subscriptionId);
    }

    public List<Subscription> getSubscriptions(Member member) {
        return em.createQuery("select s from Subscription s where s.order.member = :member and s.isValid = :isValid", Subscription.class)
                .setParameter("member", member)
                .setParameter("isValid", true)
                .getResultList();
    }
}
