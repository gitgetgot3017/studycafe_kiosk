package lhj.studycafe_kiosk.vote;

import lhj.studycafe_kiosk.domain.*;
import lhj.studycafe_kiosk.vote.dto.VoteRequest;
import lhj.studycafe_kiosk.vote.dto.VoteResultDto;
import lhj.studycafe_kiosk.vote.exception.AlreadyVoteException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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

        validateVoteYn(member, voteTitle); // 투표 전, 이미 투표를 했는지 검증

        for (Long voteOptionId : voteRequest.getVoteOptionIds()) {
            VoteOption voteOption = voteRepository.getVoteOption(voteOptionId);
            voteRepository.saveVote(new Vote(voteTitle, voteOption, member));
        }
    }

    private void validateVoteYn(Member member, VoteTitle voteTitle) {

        List<Vote> votes = voteRepository.getVoteYn(member, voteTitle);
        if (!votes.isEmpty()) {
            throw new AlreadyVoteException("[" + voteTitle.getTitle() + "] 주제에 대해 이미 투표 완료하였습니다!");
        }
    }

    public List<VoteResultDto> checkVoteResult() {

        return voteRepository.getVoteResult();
    }
}
