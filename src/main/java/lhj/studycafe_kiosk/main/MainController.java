package lhj.studycafe_kiosk.main;

import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.UsageStatus;
import lhj.studycafe_kiosk.domain.UserInOut;
import lhj.studycafe_kiosk.main.dto.MainInOutResponse;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.subscription.SubscriptionRepository;
import lhj.studycafe_kiosk.usage_status.UsageStatusRepository;
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
@RequestMapping("/main")
public class MainController {

    private final MemberRepository memberRepository;
    private final UsageStatusRepository usageStatusRepository;
    private final SubscriptionRepository subscriptionRepository;

    @GetMapping
    public HttpEntity<MainInOutResponse> getMainInOut(@SessionAttribute(value = "loginMember", required = false) Long memberId) {

        MainInOutResponse mainInOutResponse = new MainInOutResponse();

        if (memberId == null) {
            mainInOutResponse.setMainInOut(false);
            return new ResponseEntity<>(mainInOutResponse, HttpStatus.OK);
        }
        Member member = memberRepository.getMember(memberId);

        try {
            UsageStatus usageStatus = usageStatusRepository.getUsageStatus(member);
            subscriptionRepository.getRepresentativeSubscription(member);

            if (usageStatus.getUserInOut() == UserInOut.IN) { // 대표 이용권이 존재하고 IN(입실) 상태인 경우, MainIn.js를 보여준다.
                mainInOutResponse.setMainInOut(true);
            }
        } catch (EmptyResultDataAccessException e) {
            mainInOutResponse.setMainInOut(false);
        }

        return new ResponseEntity<>(mainInOutResponse, HttpStatus.OK);
    }
}
