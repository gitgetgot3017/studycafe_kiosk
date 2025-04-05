package lhj.studycafe_kiosk.domain.notification.controller;

import lhj.studycafe_kiosk.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(produces = "text/event-stream")
    public SseEmitter subscribe(@SessionAttribute("loginMember") Long memberId, @RequestHeader(value = "Last-Event-ID", defaultValue = "") String lastEventId) {

        return notificationService.subscribe(memberId, lastEventId);
    }
}
