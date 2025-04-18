package lhj.studycafekiosk.domain.seat.domain;

import jakarta.persistence.*;
import lhj.studycafekiosk.domain.member.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Seat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    private LocalDateTime endDateTime;

    public void changeSeatState(Member member, LocalDateTime endDateTime) {
        this.member = member;
        this.endDateTime = endDateTime;
    }
}
