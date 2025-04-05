package lhj.studycafekiosk;

import lhj.studycafekiosk.domain.seat.repository.SeatRepository;
import lhj.studycafekiosk.domain.seat.service.SeatService;
import lhj.studycafekiosk.domain.usagestatus.repository.UsageStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static lhj.studycafekiosk.TaskType.ENTRACELIMIT;

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
