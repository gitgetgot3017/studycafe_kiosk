package lhj.studycafe_kiosk.coupon;

import lhj.studycafe_kiosk.domain.Coupon;
import lhj.studycafe_kiosk.domain.CouponType;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@AllArgsConstructor
public class BirthdayCouponScheduler {

    private final MemberRepository memberRepository;
    private final CouponService couponService;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행되는 스케줄러
    public void issueBirthdayCoupon() {

        List<Member> members = memberRepository.getAllMembers();
        for (Member member : members) {
            if (couponService.isBirthCouponIssueDate(member.getBirth())) {
                LocalDateTime issueDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
                Coupon coupon = new Coupon(member, "[생일을 축하합니다!] 생일 맞이 10% 할인 쿠폰", CouponType.RATE, 10, false, issueDateTime, issueDateTime.plusDays(14), issueDateTime, null);
                couponService.issueCoupon(coupon);
            }
        }
    }
}
