package lhj.studycafe_kiosk;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class NotificationEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_event_id")
    private Long eventId;

    private String id;

    private String message;

    private boolean pushed; // 유저에게 알림이 전송되었는지 여부

    private LocalDateTime notificationDateTime;

    public NotificationEvent(String id, String message, boolean pushed, LocalDateTime notificationDateTime) {
        this.id = id;
        this.message = message;
        this.pushed = pushed;
        this.notificationDateTime = notificationDateTime;
    }

    public void pushedToUser() {
        pushed = true;
    }
}
