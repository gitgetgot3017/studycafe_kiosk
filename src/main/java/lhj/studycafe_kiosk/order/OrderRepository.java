package lhj.studycafe_kiosk.order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}
