package lhj.studycafekiosk.domain.vote.controller;

import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.vote.service.VoteService;
import lhj.studycafekiosk.domain.vote.dto.VoteRegisterDto;
import lhj.studycafekiosk.domain.vote.dto.VoteRequest;
import lhj.studycafekiosk.domain.vote.dto.VoteResultDto;
import lhj.studycafekiosk.domain.vote.domain.VoteTitleWithOptions;
import lhj.studycafekiosk.domain.member.repository.MemberRepository;
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

    @GetMapping("/result")
    public HttpEntity<List<VoteResultDto>> checkVoteResult() {

        List<VoteResultDto> voteResultDtoList = voteService.checkVoteResult();
        return new ResponseEntity<>(voteResultDtoList, HttpStatus.OK);
    }

    @PostMapping("/register")
    public void registerVote(@RequestBody @Validated VoteRegisterDto voteRegisterDto) {

        voteService.registerVote(voteRegisterDto);
    }
}
