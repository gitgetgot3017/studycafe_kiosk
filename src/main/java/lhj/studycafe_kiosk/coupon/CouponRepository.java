package lhj.studycafe_kiosk.coupon;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    @PersistenceContext
    EntityManager em;

    public void saveCoupon(Coupon coupon) {
        em.persist(coupon);
    }
}
