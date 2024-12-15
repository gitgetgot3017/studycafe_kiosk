package lhj.studycafe_kiosk.vote;

import lhj.studycafe_kiosk.domain.*;
import lhj.studycafe_kiosk.vote.dto.VoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;

    public List<VoteTitleWithOptions> getVotes() {

        List<VoteTitle> voteTitles = voteRepository.getVoteTitles();
        List<VoteOption> voteOptions = voteRepository.getVoteOptions();

        List<VoteTitleWithOptions> voteTitleWithOptions = new ArrayList<>();
        for (VoteTitle voteTitle : voteTitles) {
            List<Long> voteOptionIds = new ArrayList<>();
            List<String> contents = new ArrayList<>();
            for (VoteOption voteOption : voteOptions) {
                if (voteTitle.getId() == voteOption.getVoteTitle().getId()) {
                    voteOptionIds.add(voteOption.getId());
                    contents.add(voteOption.getContent());
                }
            }
            voteTitleWithOptions.add(new VoteTitleWithOptions(voteTitle.getId(), voteOptionIds, voteTitle.getTitle(), contents, voteTitle.isMultiple()));
        }
        return voteTitleWithOptions;
    }

    public void vote(Member member, VoteRequest voteRequest) {

        Long voteTitleId = voteRequest.getVoteTitleId();
        VoteTitle voteTitle = voteRepository.getVoteTitle(voteTitleId);

        for (Long voteOptionId : voteRequest.getVoteOptionIds()) {
            VoteOption voteOption = voteRepository.getVoteOption(voteOptionId);
            voteRepository.saveVote(new Vote(voteTitle, voteOption, member));
        }
    }
}
