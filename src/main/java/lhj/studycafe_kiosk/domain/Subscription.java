package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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

    private LocalDateTime orderDateTime;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime leftDateTime;
}
