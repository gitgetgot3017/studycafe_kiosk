package lhj.studycafe_kiosk.coupon;

import lhj.studycafe_kiosk.coupon.dto.CouponInfoResponse;
import lhj.studycafe_kiosk.coupon.exception.NotExistCouponException;
import lhj.studycafe_kiosk.domain.Coupon;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @GetMapping
    public HttpEntity<List<CouponInfoResponse>> issueCoupon(@RequestParam(value = "used", defaultValue = "false") String used, @SessionAttribute("loginMember") Long memberId) {

        Member member = memberRepository.getMember(memberId);

        List<Coupon> coupons;
        if (used.equals("false")) {
            coupons = couponRepository.getUsableCoupons(member);
        } else {
            coupons = couponRepository.getExpiredCoupon(member);
        }

        List<CouponInfoResponse> couponInfoResponses;
        if (coupons.isEmpty()) {
            couponInfoResponses = new ArrayList<>();
        } else {
            couponInfoResponses = changeAllCouponToCouponInfoResponse(coupons);
        }
        return new ResponseEntity<>(couponInfoResponses, HttpStatus.OK);
    }

    private CouponInfoResponse changeCouponToCouponInfoResponse(Coupon coupon) {

        return new CouponInfoResponse(coupon.getMember(), coupon.getName(), coupon.getCouponType(), coupon.getRateOrHour(), coupon.isUsed(), getFormattedEndDateTime(coupon.getStartDatetime()), getFormattedEndDateTime(coupon.getEndDatetime()), getFormattedEndDateTime(coupon.getIssueDatetime()));
    }

    private List<CouponInfoResponse> changeAllCouponToCouponInfoResponse(List<Coupon> coupons) {

        List<CouponInfoResponse> couponInfoResponses = new ArrayList<>();
        for (Coupon coupon : coupons) {
            couponInfoResponses.add(changeCouponToCouponInfoResponse(coupon));
        }
        return couponInfoResponses;
    }

    private String getFormattedEndDateTime(LocalDateTime dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        return dateTime.format(formatter);
    }
}
