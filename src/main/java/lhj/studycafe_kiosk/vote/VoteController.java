package lhj.studycafe_kiosk.vote;

import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.VoteTitleWithOptions;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.vote.dto.VoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;
    private final MemberRepository memberRepository;

    @GetMapping
    public HttpEntity<List<VoteTitleWithOptions>> getVotes() {

        List<VoteTitleWithOptions> voteTitleWithOptions = voteService.getVotes();
        return new ResponseEntity<>(voteTitleWithOptions, HttpStatus.OK);
    }

    @PostMapping
    public void vote(@SessionAttribute("loginMember") Long memberId, @RequestBody @Validated VoteRequest voteRequest) {

        Member member = memberRepository.getMember(memberId);
        voteService.vote(member, voteRequest);
    }
}
