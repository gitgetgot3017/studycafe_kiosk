package lhj.studycafe_kiosk.vote;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Vote;
import lhj.studycafe_kiosk.domain.VoteOption;
import lhj.studycafe_kiosk.domain.VoteTitle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VoteRepository {

    @PersistenceContext
    EntityManager em;

    public List<VoteTitle> getVoteTitles() {
        return em.createQuery("select vt from VoteTitle vt", VoteTitle.class)
                .getResultList();
    }

    public VoteTitle getVoteTitle(Long voteTitleId) {
        return em.find(VoteTitle.class, voteTitleId);
    }

    public List<VoteOption> getVoteOptions() {
        return em.createQuery("select vo from VoteOption vo", VoteOption.class)
                .getResultList();
    }

    public VoteOption getVoteOption(Long voteOptionId) {
        return em.find(VoteOption.class, voteOptionId);
    }

    public void saveVote(Vote vote) {
        em.persist(vote);
    }

    public List<Vote> getVoteYn(Member member, VoteTitle voteTitle) {
        return em.createQuery("select v from Vote v where v.member = :member and v.voteTitle = :voteTitle", Vote.class)
                .setParameter("member", member)
                .setParameter("voteTitle", voteTitle)
                .getResultList();
    }
}
