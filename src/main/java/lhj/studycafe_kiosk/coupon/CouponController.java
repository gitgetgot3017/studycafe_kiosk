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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @GetMapping
    public HttpEntity<List<CouponInfoResponse>> issueCoupon(@RequestParam("used") String used, @SessionAttribute("loginMember") Long memberId, @PathVariable("couponId") Long couponId) {

        Member member = memberRepository.getMember(30L);

        List<Coupon> coupons;
        if (used.equals("false")) {
            coupons = couponRepository.getUsableCoupons(member);
            System.out.println("coupons = " + coupons);
        } else {
            coupons = couponRepository.getExpiredCoupon(member);
            System.out.println("coupons = " + coupons);
        }

        if (coupons.isEmpty()) {
            throw new NotExistCouponException("쿠폰이 존재하지 않습니다.");
        }
        List<CouponInfoResponse> couponInfoResponses = changeAllCouponToCouponInfoResponse(coupons);
        return new ResponseEntity<>(couponInfoResponses, HttpStatus.OK);
    }

    private CouponInfoResponse changeCouponToCouponInfoResponse(Coupon coupon) {

        return new CouponInfoResponse(coupon.getMember(), coupon.getName(), coupon.getCouponType(), coupon.getRateOrHour(), coupon.isUsed(), coupon.getStartDatetime(), coupon.getEndDatetime(), coupon.getIssueDatetime());
    }

    private List<CouponInfoResponse> changeAllCouponToCouponInfoResponse(List<Coupon> coupons) {

        List<CouponInfoResponse> couponInfoResponses = new ArrayList<>();
        for (Coupon coupon : coupons) {
            couponInfoResponses.add(changeCouponToCouponInfoResponse(coupon));
        }
        return couponInfoResponses;
    }
}
