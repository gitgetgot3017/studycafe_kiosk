package lhj.studycafe_kiosk.domain.vote.service;

import lhj.studycafe_kiosk.domain.member.domain.Member;
import lhj.studycafe_kiosk.domain.vote.domain.Vote;
import lhj.studycafe_kiosk.domain.vote.domain.VoteOption;
import lhj.studycafe_kiosk.domain.vote.domain.VoteTitle;
import lhj.studycafe_kiosk.domain.vote.domain.VoteTitleWithOptions;
import lhj.studycafe_kiosk.domain.vote.repository.VoteRepository;
import lhj.studycafe_kiosk.domain.vote.dto.VoteRegisterDto;
import lhj.studycafe_kiosk.domain.vote.dto.VoteRequest;
import lhj.studycafe_kiosk.domain.vote.dto.VoteResultDto;
import lhj.studycafe_kiosk.domain.vote.exception.AlreadyVoteException;
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

    public void registerVote(VoteRegisterDto voteRegisterDto) {

        // VoteTitle 등록
        VoteTitle voteTitle = new VoteTitle(voteRegisterDto.getVoteTitle(), voteRegisterDto.isMultiple());
        voteRepository.saveVoteTitle(voteTitle);

        // 여러 VoteOption 등록
        for (String voteOptionContent : voteRegisterDto.getVoteOptions()) {
            if (voteOptionContent.equals("")) {
                continue;
            }
            VoteOption voteOption = new VoteOption(voteTitle, voteOptionContent);
            voteRepository.saveVoteOption(voteOption);
        }
    }
}
