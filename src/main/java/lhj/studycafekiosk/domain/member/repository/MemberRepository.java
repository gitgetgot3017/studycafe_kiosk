package lhj.studycafekiosk.domain.member.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafekiosk.domain.member.dto.ChangeMemberInfoRequest;
import lhj.studycafekiosk.domain.member.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<Member> getSaltByPhone(String phone) {
        Member member = em.createQuery("select m from Member m where m.phone = :phone", Member.class)
                .setParameter("phone", phone)
                .getSingleResult();
        return Optional.of(member);
    }

    public List<Member> getLoginMember(String phone, String password) {
         return em.createQuery("select m from Member m where m.phone = :phone and m.password = :password", Member.class)
                .setParameter("phone", phone)
                .setParameter("password", password)
                .getResultList();
    }

    public Member getMember(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public void updateMember(Long memberId, String type, ChangeMemberInfoRequest changeMember) {

        Member member = getMember(memberId);

        if (type.equals("general")) {
            member.changeGeneralInfo(changeMember.getName(), changeMember.getBirth());
        } else if (type.equals("phone")) {
            member.setPhone(changeMember.getPhone());
        } else if (type.equals("password")) {
            member.setPassword(changeMember.getNewPassword());
        }
    }

    public List<Member> getAllMembers() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Member getExistMember(String subPhone, String subPassword) {
        return em.createQuery("select m from Member m where function('RIGHT', m.phone, 4) = :subPhone and function('LEFT', m.password, 2) = :subPassword", Member.class)
                .setParameter("subPhone", subPhone)
                .setParameter("subPassword", subPassword)
                .getSingleResult();
    }

    public void updatePassword(Member member, String newPassword) {
        member.setPassword(newPassword);
    }

    public void updatePhone(Member member, String phone) {
        member.setPhone(phone);
    }
}
