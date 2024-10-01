package lhj.studycafe_kiosk.coupon;

import lhj.studycafe_kiosk.domain.Coupon;
import lhj.studycafe_kiosk.domain.CouponType;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    public boolean isBirthCouponIssueDate(LocalDate birth) {

        LocalDate today = LocalDate.now();
        LocalDate oneWeekBeforeBirth = LocalDate.of(today.getYear(), birth.getMonthValue(), birth.getDayOfMonth()).minusDays(7);

        return today.equals(oneWeekBeforeBirth);
    }

    public void issueCoupon(Coupon coupon) {
        couponRepository.saveCoupon(coupon);
    }

    public void issueJoinCoupon(Member member) {

        LocalDateTime issueDateTime = LocalDateTime.now();
        Coupon coupon = new Coupon(member, "[가입을 측하합니다!] 회원 맞이 1시간 이용권 증정", CouponType.HOUR, 1, false, issueDateTime, issueDateTime.plusHours(24), issueDateTime, null);
        couponRepository.saveCoupon(coupon);
    }

    public void issueCouponBasedCumulativeAmount(Member member) {

        Optional<Long> opCumulativeAmount = orderRepository.getCumulativeAmount(member);
        Long cumulativeAmount = 0L;
        if (opCumulativeAmount.isPresent()) {
            cumulativeAmount = opCumulativeAmount.get();
        }

        if (cumulativeAmount < 300_000) {
            return;
        }

        LocalDateTime issueDateTime = LocalDateTime.now();
        Coupon coupon = new Coupon(member, CouponType.RATE, false, issueDateTime, issueDateTime.plusDays(28), issueDateTime, null);
        String couponName = "[구매에 감사드립니다!] ";
        int rateOrHour = 0;
        if (cumulativeAmount < 500_000) {
            couponName += "3% 할인 쿠폰";
            rateOrHour = 3;
        } else if (cumulativeAmount < 1_000_000) {
            couponName += "5% 할인 쿠폰";
            rateOrHour = 5;
        } else if (cumulativeAmount < 1_500_000) {
            couponName += "7% 할인 쿠폰";
            rateOrHour = 7;
        } else if (cumulativeAmount < 2_000_000) {
            couponName += "10% 할인 쿠폰";
            rateOrHour = 10;
        } else {
            couponName += "15% 할인 쿠폰";
            rateOrHour = 15;
        }
        coupon.setCouponSubField(couponName, rateOrHour);
        couponRepository.saveCoupon(coupon);
    }

    public void changeCouponStatus(Long couponId) {
        couponRepository.updateCouponStatus(couponId);
    }
}
