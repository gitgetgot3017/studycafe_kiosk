package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    private String content;

    @ManyToOne
    @JoinColumn(name = "chat_member_id")
    private ChatMember chatMember;

    private LocalDateTime sentDateTime;

    public ChatMessage(ChatType chatType, String content, ChatMember chatMember, LocalDateTime sentDateTime) {
        this.chatType = chatType;
        this.content = content;
        this.chatMember = chatMember;
        this.sentDateTime = sentDateTime;
    }
}
