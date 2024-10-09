package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class UsageStatus {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_status_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    private UserInOut userInOut;

    private LocalDateTime userDateTime;
}
