package lhj.studycafe_kiosk.vote;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Vote;
import lhj.studycafe_kiosk.domain.VoteOption;
import lhj.studycafe_kiosk.domain.VoteTitle;
import lhj.studycafe_kiosk.vote.dto.VoteOptionResultDto;
import lhj.studycafe_kiosk.vote.dto.VoteResultDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<VoteResultDto> getVoteResult() {
        List<Tuple> tuples = em.createQuery("select v.voteTitle.title as voteTitle, v.voteOption.content as voteOption, count(v) as count from Vote v group by v.voteTitle.title, v.voteOption.content", Tuple.class)
                .getResultList();

        Map<String, List<VoteOptionResultDto>> groupedResults = tuples.stream()
            .collect(Collectors.groupingBy(
                tuple -> tuple.get("voteTitle", String.class),
                Collectors.mapping(
                    tuple -> new VoteOptionResultDto(
                            tuple.get("voteOption", String.class),
                            tuple.get("count", Long.class)
                    ),
                    Collectors.toList()
                )
            ));

        return groupedResults.entrySet().stream()
                .map(entry -> new VoteResultDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
