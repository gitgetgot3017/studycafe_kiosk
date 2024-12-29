package lhj.studycafe_kiosk.notification;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class NotificationRepository {

    private Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>(); // SseEmitter 저장소
    private Map<String, String> events = new ConcurrentHashMap(); // 이벤트 저장소 TODO: 추후 DB로 변경 예정

    public void saveSseEmitter(Long memberId, SseEmitter sseEmitter) {
        sseEmitters.put(memberId, sseEmitter);
    }

    public void deleteSseEmitter(Long id) {
        sseEmitters.remove(id);
    }

    public SseEmitter findSseEmitter(Long memberId) {
        return sseEmitters.get(memberId);
    }

    public Map<String, String> findAllEvents(Long memberId) {

        Map<String, String> result = new ConcurrentHashMap<>();
        for (Map.Entry<String, String> entry : result.entrySet()) {
            if (entry.getKey().startsWith(memberId + "_")) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public void saveEvent(String id, String event) {
        events.put(id, event);
    }
}
