package lhj.studycafe_kiosk.domain.order.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.member.domain.Member;
import lhj.studycafe_kiosk.domain.order.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public class OrderRepository {

    @PersistenceContext
    EntityManager em;

    public Optional<Long> getCumulativeAmount(Member member) {
        Long result = em.createQuery("select sum(o.price) from Order o where o.member = :member", Long.class)
                .setParameter("member", member)
                .getSingleResult();
        return Optional.ofNullable(result);
    }

    public Long saveOrder(Order order) {
        em.persist(order);
        return order.getId();
    }

    public Order getOrder(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public List<Order> getOrders(Member member) {
        return em.createQuery("select o from Order o where o.member = :member", Order.class)
                .setParameter("member", member)
                .getResultList();
    }

    public Order getItemByMember(Member member) {
        return em.createQuery("select o from Order o where o.member = :member order by o.orderDatetime desc", Order.class)
                .setParameter("member", member)
                .setMaxResults(1)
                .getSingleResult();
    }
}
