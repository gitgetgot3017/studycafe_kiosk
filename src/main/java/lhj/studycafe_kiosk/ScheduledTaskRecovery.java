package lhj.studycafe_kiosk;

import lhj.studycafe_kiosk.domain.ScheduledTask;
import lhj.studycafe_kiosk.seat.EnterCheckTask;
import lhj.studycafe_kiosk.seat.SeatRepository;
import lhj.studycafe_kiosk.seat.SeatService;
import lhj.studycafe_kiosk.seat.VacateSeatTask;
import lhj.studycafe_kiosk.usage_status.UsageStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static lhj.studycafe_kiosk.domain.TaskType.ENTRACELIMIT;

@Component
@RequiredArgsConstructor
public class ScheduledTaskRecovery {

    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final UsageStatusRepository usageStatusRepository;
    private final TaskScheduler taskScheduler;

    @EventListener(ApplicationReadyEvent.class)
    private void restoreScheduledTask() {

        List<ScheduledTask> scheduledTasks = seatRepository.getAllScheduledTask();
        for (ScheduledTask scheduledTask : scheduledTasks) {

            if (scheduledTask.getType() == ENTRACELIMIT) {
                taskScheduler.schedule(
                    new EnterCheckTask(scheduledTask.getMember(), usageStatusRepository, seatService),
                    scheduledTask.getExecutionTime().atZone(ZoneId.of("Asia/Seoul")).toInstant());
            } else {
                taskScheduler.schedule(
                    new VacateSeatTask(scheduledTask.getMember(), seatService),
                    scheduledTask.getExecutionTime().atZone(ZoneId.of("Asia/Seoul")).toInstant());
            }

            if (scheduledTask.getExecutionTime().isBefore(LocalDateTime.now())) {
                seatService.removeScheduledTask(scheduledTask);
            }
        }
    }
}
