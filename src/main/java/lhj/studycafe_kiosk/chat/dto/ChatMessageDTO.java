package lhj.studycafe_kiosk.chat.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageDTO {

    private String content;
    private Long senderId;
    private LocalDateTime sentDateTime;

    public ChatMessageDTO(String content, Long senderId, LocalDateTime sentDateTime) {
        this.content = content;
        this.senderId = senderId;
        this.sentDateTime = sentDateTime;
    }
}
