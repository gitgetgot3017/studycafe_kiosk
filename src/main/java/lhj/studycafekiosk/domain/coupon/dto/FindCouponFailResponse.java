package lhj.studycafekiosk.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindCouponFailResponse {

    private String domain;
    private String message;
}
