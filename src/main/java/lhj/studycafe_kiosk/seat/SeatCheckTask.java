package lhj.studycafe_kiosk.seat;

import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.UsageStatus;
import lhj.studycafe_kiosk.domain.UserInOut;
import lhj.studycafe_kiosk.usage_status.UsageStatusRepository;
import org.springframework.dao.EmptyResultDataAccessException;

public class SeatCheckTask implements Runnable {

    private final Member member;
    private final UsageStatusRepository usageStatusRepository;
    private final SeatService seatService;

    public SeatCheckTask(Member member, UsageStatusRepository usageStatusRepository, SeatService seatService) {
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
