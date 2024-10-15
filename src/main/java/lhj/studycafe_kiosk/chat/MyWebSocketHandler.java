package lhj.studycafe_kiosk.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lhj.studycafe_kiosk.domain.ChatMember;
import lhj.studycafe_kiosk.domain.ChatMessage;
import lhj.studycafe_kiosk.domain.ChatType;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.member.exception.NotExistMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MyWebSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> socketSessionList = new LinkedList<>();

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Map<String, Object> attributes = session.getAttributes();
        Long memberId = (Long) attributes.get("memberId");

        // 채팅방에 처음 입장하는 경우, (1) ChatMember에 포함시키기 (2) 입장 메시지를 DB에 저장
        if (memberId == null) {
            throw new NotExistMemberException("로그인을 다시 해주세요.");
        }

        Member member = memberRepository.getMember(memberId);
        ChatMember chatMember;
        try {
            chatMember = chatRepository.findChatMember(member);
        } catch (EmptyResultDataAccessException e) { // 채팅방에 처음 입장한 유저에 대한 처리

            chatMember = new ChatMember(member, LocalDateTime.now());
            chatRepository.saveChatMember(chatMember);

            ChatMessage chatMessage = new ChatMessage(ChatType.ENTER, member.getName() + " 님이 입장하였습니다.", chatMember, LocalDateTime.now());
            chatRepository.saveEnterMessage(chatMessage);

            socketSessionList.add(session);
        }

        // DB에서 채팅 메시지 가져오기
        List<ChatMessage> chatMessages = chatRepository.findChatMember(chatMember);
        String jsonChatMessages = objectMapper.writeValueAsString(chatMessages);
        session.sendMessage(new TextMessage(jsonChatMessages));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // 서버로 전달된 채팅 메시지를 DB에 저장하기


        // 채팅방에 존재하는 모든 멤버에게 채팅 메시지 전송

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }
}
