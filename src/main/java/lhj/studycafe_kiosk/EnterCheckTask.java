package lhj.studycafe_kiosk;

import lhj.studycafe_kiosk.domain.member.domain.Member;
import lhj.studycafe_kiosk.domain.seat.service.SeatService;
import lhj.studycafe_kiosk.domain.usage_status.domain.UsageStatus;
import lhj.studycafe_kiosk.domain.main.domain.UserInOut;
import lhj.studycafe_kiosk.domain.usage_status.repository.UsageStatusRepository;
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
