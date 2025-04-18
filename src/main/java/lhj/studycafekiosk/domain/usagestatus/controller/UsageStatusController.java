package lhj.studycafekiosk.domain.usagestatus.controller;

import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.usagestatus.repository.UsageStatusRepository;
import lhj.studycafekiosk.domain.usagestatus.dto.UsageStatusResponse;
import lhj.studycafekiosk.domain.usagestatus.domain.UsageStatus;
import lhj.studycafekiosk.domain.member.repository.MemberRepository;
import lhj.studycafekiosk.domain.usagestatus.exception.NotExistUserInOutException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usage-status")
public class UsageStatusController {

    private final UsageStatusRepository usageStatusRepository;
    private final MemberRepository memberRepository;

    @GetMapping
    public HttpEntity<UsageStatusResponse> method(@SessionAttribute("loginMember") Long memberId) {

        try {
            Member member = memberRepository.getMember(memberId);
            UsageStatus usageStatus = usageStatusRepository.getUsageStatus(member);
            UsageStatusResponse usageStatusResponse = new UsageStatusResponse(usageStatus.getUserInOut().toString());
            return new ResponseEntity(usageStatusResponse, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e.getClass());
            throw new NotExistUserInOutException("입퇴실 기록이 존재하지 않습니다.");
        }
    }
}
