package lhj.studycafe_kiosk.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    EntityManager em;

    public void saveMember(Member member) {
        em.persist(member);
    }

    public List<Member> getJoinMember(String phone) {
         return em.createQuery("select m from Member m where m.phone = :phone", Member.class)
                .setParameter("phone", phone)
                .getResultList();
    }

    public List<Member> getLoginMember(String phone, String password) {
         return em.createQuery("select m from Member m where m.phone = :phone and m.password = :password", Member.class)
                .setParameter("phone", phone)
                .setParameter("password", password)
                .getResultList();
    }
}
