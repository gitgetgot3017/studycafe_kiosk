package lhj.studycafekiosk.domain.main.controller;

import lhj.studycafekiosk.domain.main.dto.EntranceRequest;
import lhj.studycafekiosk.domain.main.dto.EntranceSuccessRequest;
import lhj.studycafekiosk.domain.main.dto.MainInOutResponse;
import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.subscription.domain.Subscription;
import lhj.studycafekiosk.domain.usagestatus.domain.UsageStatus;
import lhj.studycafekiosk.domain.main.domain.UserInOut;
import lhj.studycafekiosk.domain.member.repository.MemberRepository;
import lhj.studycafekiosk.domain.member.exception.LoginFailException;
import lhj.studycafekiosk.domain.seat.repository.SeatRepository;
import lhj.studycafekiosk.domain.seat.exception.NotExistSeatException;
import lhj.studycafekiosk.domain.subscription.repository.SubscriptionRepository;
import lhj.studycafekiosk.domain.subscription.exception.NotExistSubscriptionException;
import lhj.studycafekiosk.domain.usagestatus.repository.UsageStatusRepository;
import lhj.studycafekiosk.domain.usagestatus.service.UsageStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static lhj.studycafekiosk.TaskType.ENTRACELIMIT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MemberRepository memberRepository;
    private final UsageStatusService usageStatusService;
    private final UsageStatusRepository usageStatusRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SeatRepository seatRepository;

    @GetMapping
    public HttpEntity<MainInOutResponse> getMainInOut(@SessionAttribute(value = "loginMember", required = false) Long memberId) {

        MainInOutResponse mainInOutResponse = new MainInOutResponse();

        if (memberId == null) {
            mainInOutResponse.setMainInOut(false);
            mainInOutResponse.setLogin(false);
            return new ResponseEntity<>(mainInOutResponse, HttpStatus.OK);
        }
        Member member = memberRepository.getMember(memberId);

        try {
            UsageStatus usageStatus = usageStatusRepository.getUsageStatus(member);
            subscriptionRepository.getSubscription(member);

            if (usageStatus.getUserInOut() == UserInOut.IN) { // 대표 이용권이 존재하고 IN(입실) 상태인 경우, MainIn.js를 보여준다.
                mainInOutResponse.setMainInOut(true);
            }
        } catch (EmptyResultDataAccessException e) {
            mainInOutResponse.setMainInOut(false);
        }

        mainInOutResponse.setLogin(true);
        mainInOutResponse.setMemberGrade(member.getGrade());
        return new ResponseEntity<>(mainInOutResponse, HttpStatus.OK);
    }

    @PostMapping("/entrance")
    public HttpEntity<EntranceSuccessRequest> enter(@RequestBody @Validated EntranceRequest entranceRequest) {

        // 존재하는 유저인지 확인
        Member member = validateExistMember(entranceRequest);

        // 유저가 이용권을 갖고 있는지 확인
        Subscription subscription;
        try {
            subscription = subscriptionRepository.getSubscription(member);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistSubscriptionException("보유 중인 이용권이 존재하지 않습니다.");
        }

        // 선택한 좌석이 있는지 확인
        try {
            seatRepository.getMySeat(member);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistSeatException("좌석을 선택해주세요.");
        }

        // 입실 처리(usage_status에 IN 기록 남기기)
        usageStatusService.recordUsageStatus(new UsageStatus(subscription, member, UserInOut.IN, LocalDateTime.now()));

        // 해당 좌석에 대해 10분 내 입실 확인 예약이 DB에 등록되어 있으면 취소한다.
        seatRepository.deleteScheduledTaskByMemberAndType(member, ENTRACELIMIT);

        return new ResponseEntity(new EntranceSuccessRequest("입실 완료하였습니다."), HttpStatus.OK);
    }

    private Member validateExistMember(EntranceRequest entranceRequest) {

        try {
            return memberRepository.getExistMember(entranceRequest.getPhone(), entranceRequest.getPassword());
        } catch (EmptyResultDataAccessException e) {
            throw new LoginFailException("전화번호 혹은 비밀번호를 잘못 입력하셨습니다.");
        }
    }
}
