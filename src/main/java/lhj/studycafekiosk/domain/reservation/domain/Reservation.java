package lhj.studycafekiosk.domain.reservation.domain;

import jakarta.persistence.*;
import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.seat.domain.Seat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private LocalDateTime reservationDateTime;

    private boolean used; // 예약이 실제 사용으로 이어졌는지 여부

    private boolean finished; // 예약 서비스가 종료되었는지 여부

    public Reservation(Member member, Seat seat, LocalDateTime reservationDateTime, boolean used, boolean finished) {
        this.member = member;
        this.seat = seat;
        this.reservationDateTime = reservationDateTime;
        this.used = used;
        this.finished = finished;
    }
}
