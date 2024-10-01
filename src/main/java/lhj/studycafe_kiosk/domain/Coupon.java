package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "coupon_name")
    private String name;

    private CouponType couponType;

    private int rateOrHour;

    private boolean isUsed;

    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    private LocalDateTime issueDatetime;

    private LocalDateTime usageDateTime;

    public Coupon(Member member, String name, CouponType couponType, int rateOrHour, boolean isUsed, LocalDateTime startDatetime, LocalDateTime endDatetime, LocalDateTime issueDatetime) {
        this.member = member;
        this.name = name;
        this.couponType = couponType;
        this.rateOrHour = rateOrHour;
        this.isUsed = isUsed;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.issueDatetime = issueDatetime;
    }

    public Coupon(Member member, CouponType couponType, boolean isUsed, LocalDateTime startDatetime, LocalDateTime endDatetime, LocalDateTime issueDatetime) {
        this.member = member;
        this.couponType = couponType;
        this.isUsed = isUsed;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.issueDatetime = issueDatetime;
    }

    public void setCouponSubField(String name, int rateOrHour) {
        this.name = name;
        this.rateOrHour = rateOrHour;
    }

    public void changeCouponStatus(boolean isUsed, LocalDateTime usageDateTime) {
        this.isUsed = isUsed;
        this.usageDateTime = usageDateTime;
    }
}
