package lhj.studycafe_kiosk;

import lhj.studycafe_kiosk.domain.member.domain.Member;
import lhj.studycafe_kiosk.domain.seat.service.SeatService;

public class VacateSeatTask implements Runnable {

    private final Member member;
    private final SeatService seatService;

    public VacateSeatTask(Member member, SeatService seatService) {
        this.member = member;
        this.seatService = seatService;
    }

    @Override
    public void run() {
        seatService.vacateSeat(member);
    }
}
