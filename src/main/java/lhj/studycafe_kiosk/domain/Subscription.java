package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Subscription {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime; // 일일권, 충전권, 기간권, 고정석에서 사용

    private Duration leftTime; // 충전권에서만 사용

    private boolean isValid;

    public Subscription(Member member, LocalDateTime startDateTime, LocalDateTime endDateTime, Duration leftTime, boolean isValid) {
        this.member = member;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.leftTime = leftTime;
        this.isValid = isValid;
    }

    public void setLeftTime(Duration leftTime) {
        this.leftTime = leftTime;
    }

    public void setSubscriptionInvalid() {
        isValid = false;
    }

    public void extendSubscriptionHours(int hours) {
        endDateTime = endDateTime.plusHours(hours);
    }

    public void extendSubscriptionDays(int days) {
        endDateTime = endDateTime.plusDays(days);
    }
}
