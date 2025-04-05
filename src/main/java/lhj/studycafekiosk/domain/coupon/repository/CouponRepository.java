package lhj.studycafekiosk.domain.coupon.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafekiosk.domain.coupon.domain.Coupon;
import lhj.studycafekiosk.domain.member.domain.Member;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CouponRepository {

    @PersistenceContext
    EntityManager em;

    public void saveCoupon(Coupon coupon) {
        em.persist(coupon);
    }

    public List<Coupon> getUsableCoupons(Member member) {
        return em.createQuery("select c from Coupon c where c.member = :member and c.isUsed = :isUsed and c.startDatetime <= :curDatetime and :curDatetime <= c.endDatetime", Coupon.class)
                .setParameter("member", member)
                .setParameter("isUsed", false)
                .setParameter("curDatetime", LocalDateTime.now())
                .getResultList();
    }

    public List<Coupon> getExpiredCoupon(Member member) {
        return em.createQuery("select c from Coupon c where c.member = :member and c.isUsed = :isUsed and (c.startDatetime > :curDatetime or :curDatetime > c.endDatetime)", Coupon.class)
                .setParameter("member", member)
                .setParameter("isUsed", true)
                .setParameter("curDatetime", LocalDateTime.now())
                .getResultList();
    }

    public Optional<Coupon> getCoupon(Long couponId) {
        Coupon opCoupon = em.find(Coupon.class, couponId);
        return Optional.ofNullable(opCoupon);
    }

    public void updateCouponStatus(Long couponId) {
        Coupon coupon = getCoupon(couponId).get();
        coupon.changeCouponStatus(true, LocalDateTime.now());
    }

    public Coupon getSpecificCoupon(Member member, int discountRate) {
        return em.createQuery("select c from Coupon c where c.member = :member and c.name like concat('%', :discountRate, '%')", Coupon.class)
                .setParameter("member", member)
                .setParameter("discountRate", discountRate)
                .getSingleResult();
    }
}
