package lhj.studycafekiosk.domain.coupon.dto;

import lhj.studycafekiosk.domain.coupon.domain.CouponType;
import lhj.studycafekiosk.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponInfoResponse {

    private Long couponId;
    private Member member;
    private String name;
    private CouponType couponType;
    private int rateOrHour;
    private boolean isUsed;
    private String startDatetime;
    private String endDatetime;
    private String issueDatetime;
}
