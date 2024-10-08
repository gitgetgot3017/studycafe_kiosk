package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
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
}
