package lhj.studycafe_kiosk.vote;

import lhj.studycafe_kiosk.domain.VoteOption;
import lhj.studycafe_kiosk.domain.VoteTitle;
import lhj.studycafe_kiosk.domain.VoteTitleWithOptions;
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
            List<String> contents = new ArrayList<>();
            for (VoteOption voteOption : voteOptions) {
                if (voteTitle.getId() == voteOption.getVoteTitle().getId()) {
                    contents.add(voteOption.getContent());
                }
            }
            voteTitleWithOptions.add(new VoteTitleWithOptions(voteTitle.getTitle(), contents, voteTitle.isMultiple()));
        }
        return voteTitleWithOptions;
    }
}
