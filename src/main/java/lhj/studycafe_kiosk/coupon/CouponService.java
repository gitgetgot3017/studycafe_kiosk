package lhj.studycafe_kiosk.coupon;

import lhj.studycafe_kiosk.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;

    public boolean isBirthCouponIssueDate(LocalDate birth) {

        LocalDate today = LocalDate.now();
        LocalDate oneWeekBeforeBirth = LocalDate.of(today.getYear(), birth.getMonthValue(), birth.getDayOfMonth()).minusDays(7);

        return today.equals(oneWeekBeforeBirth);
    }

    public void issueCoupon(Coupon coupon) {
        couponRepository.saveCoupon(coupon);
    }
}
