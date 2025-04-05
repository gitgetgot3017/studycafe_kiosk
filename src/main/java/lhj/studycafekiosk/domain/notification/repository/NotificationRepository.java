package lhj.studycafekiosk.domain.notification.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafekiosk.NotificationEvent;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class NotificationRepository {

    @PersistenceContext
    EntityManager em;

    private Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>(); // SseEmitter 저장소

    public void saveSseEmitter(Long memberId, SseEmitter sseEmitter) {
        sseEmitters.put(memberId, sseEmitter);
    }

    public void deleteSseEmitter(Long id) {
        sseEmitters.remove(id);
    }

    public SseEmitter findSseEmitter(Long memberId) {
        return sseEmitters.get(memberId);
    }

    public Map<String, NotificationEvent> findAllEvents(Long memberId) {

        List<NotificationEvent> events = em.createQuery("select n from NotificationEvent n where n.pushed = false and n.id like :memberId", NotificationEvent.class)
                .setParameter("memberId", memberId + "_%")
                .getResultList();

        Map<String, NotificationEvent> result = new ConcurrentHashMap<>();
        for (NotificationEvent event : events) {
            result.put(event.getId(), event);
        }
        return result;
    }

    public void saveEvent(String id, String event, boolean pushed) {
        NotificationEvent notificationEvent = new NotificationEvent(id, event, pushed, LocalDateTime.now());
        em.persist(notificationEvent);
    }

    public void updateEventPushed(NotificationEvent notificationEvent) {
        notificationEvent.pushedToUser();
    }
}
