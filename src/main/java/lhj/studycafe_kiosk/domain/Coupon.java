package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "coupon_name")
    private String name;

    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    private LocalDateTime issueDatetime;
}
