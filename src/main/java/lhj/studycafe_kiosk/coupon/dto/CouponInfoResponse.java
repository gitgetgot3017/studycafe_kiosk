package lhj.studycafe_kiosk.coupon.dto;

import lhj.studycafe_kiosk.domain.CouponType;
import lhj.studycafe_kiosk.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CouponInfoResponse {

    private Member member;
    private String name;
    private CouponType couponType;
    private int rateOrHour;
    private boolean isUsed;
    private String startDatetime;
    private String endDatetime;
    private String issueDatetime;
}
