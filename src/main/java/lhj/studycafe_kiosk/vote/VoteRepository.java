package lhj.studycafe_kiosk.vote;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    public List<VoteOption> getVoteOptions() {
        return em.createQuery("select vo from VoteOption vo", VoteOption.class)
                .getResultList();
    }
}
