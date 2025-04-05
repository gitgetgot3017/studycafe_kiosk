package lhj.studycafe_kiosk;

import jakarta.persistence.*;
import lhj.studycafe_kiosk.domain.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ScheduledTask {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime executionTime;

    @Column(name = "task_type")
    @Enumerated(EnumType.STRING)
    private TaskType type;

    public ScheduledTask(Member member, LocalDateTime executionTime, TaskType type) {
        this.member = member;
        this.executionTime = executionTime;
        this.type = type;
    }
}
