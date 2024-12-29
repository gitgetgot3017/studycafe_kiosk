package lhj.studycafe_kiosk.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private static final long DEFAULT_TIMEOUT = 60L * 60 * 1000;

    public SseEmitter subscribe(Long memberId, String lastEventId) {

        // 1
        String id = memberId + "_" + System.currentTimeMillis();

        // 2
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        notificationRepository.saveSseEmitter(memberId, sseEmitter);

        sseEmitter.onCompletion(() -> notificationRepository.deleteSseEmitter(memberId));
        sseEmitter.onTimeout(() -> notificationRepository.deleteSseEmitter(memberId));

        // 3: 더미데이터 전송
        sendToClient(sseEmitter, id, "dummy data", memberId);

        // 4: 클라이언트와 서버 간 SSE 연결이 종료되어 클라이언트가 재요청한 경우
        if (!lastEventId.equals("")) {
            Map<String, String> events = notificationRepository.findAllEvents(memberId);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(sseEmitter, entry.getKey(), entry.getValue(), memberId));
        }

        return sseEmitter;
    }

    public void send(Long memberId) {

        SseEmitter sseEmitter = notificationRepository.findSseEmitter(memberId);
        String id = memberId + "_" + System.currentTimeMillis();
        String event = "예약하신 좌석 이용 가능합니다.";

        notificationRepository.saveEvent(id, event);
        sendToClient(sseEmitter, id, event, memberId);
    }

    private void sendToClient(SseEmitter sseEmitter, String id, Object data, Long memberId) {

        try {
            sseEmitter.send(SseEmitter.event()
                    .id(id)
                    .data(data));
        } catch (IOException e) {
            notificationRepository.deleteSseEmitter(memberId);
        }
    }
}
