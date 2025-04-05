package lhj.studycafekiosk;

import lhj.studycafekiosk.domain.member.domain.Member;
import lhj.studycafekiosk.domain.seat.service.SeatService;
import lhj.studycafekiosk.domain.usagestatus.domain.UsageStatus;
import lhj.studycafekiosk.domain.main.domain.UserInOut;
import lhj.studycafekiosk.domain.usagestatus.repository.UsageStatusRepository;
import org.springframework.dao.EmptyResultDataAccessException;

public class EnterCheckTask implements Runnable {

    private final Member member;
    private final UsageStatusRepository usageStatusRepository;
    private final SeatService seatService;

    public EnterCheckTask(Member member, UsageStatusRepository usageStatusRepository, SeatService seatService) {
        this.member = member;
        this.usageStatusRepository = usageStatusRepository;
        this.seatService = seatService;
    }

    @Override
    public void run() {

        boolean flag = true;
        try {
            UsageStatus usageStatus = usageStatusRepository.getUsageStatus(member);
            if (usageStatus.getUserInOut() == UserInOut.OUT) {
                flag = false;
            }
        } catch (EmptyResultDataAccessException e) {
            flag = false;
        }

        if (!flag) {
            seatService.vacateSeat(member);
        }
    }
}
