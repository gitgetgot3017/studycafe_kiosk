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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private boolean isRepresentative;

    private LocalDateTime orderDateTime;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Duration leftTime;

    public Subscription(Member member, Item item, boolean isRepresentative, LocalDateTime orderDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime, Duration leftTime) {
        this.member = member;
        this.item = item;
        this.isRepresentative = isRepresentative;
        this.orderDateTime = orderDateTime;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.leftTime = leftTime;
    }
}
