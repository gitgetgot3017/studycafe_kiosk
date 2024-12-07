package lhj.studycafe_kiosk.seat;

import lhj.studycafe_kiosk.domain.Member;

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
