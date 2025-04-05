package lhj.studycafekiosk.domain.usagestatus.domain;

import jakarta.persistence.*;
import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.subscription.domain.Subscription;
import lhj.studycafekiosk.domain.main.domain.UserInOut;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UsageStatus {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_status_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private UserInOut userInOut;

    private LocalDateTime userDateTime;

    public UsageStatus(Subscription subscription, Member member, UserInOut userInOut, LocalDateTime userDateTime) {
        this.subscription = subscription;
        this.member = member;
        this.userInOut = userInOut;
        this.userDateTime = userDateTime;
    }
}
