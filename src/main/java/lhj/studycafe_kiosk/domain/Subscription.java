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
    @JoinColumn(name = "order_id")
    private Order order;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime; // 일일권, 충전권, 기간권, 고정석에서 사용

    private Duration leftTime; // 충전권에서만 사용

    private boolean isValid;

    public Subscription(Order order, LocalDateTime startDateTime, LocalDateTime endDateTime, Duration leftTime, boolean isValid) {
        this.order = order;
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
}
