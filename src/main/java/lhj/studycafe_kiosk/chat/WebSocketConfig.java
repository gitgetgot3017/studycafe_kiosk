package lhj.studycafe_kiosk.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lhj.studycafe_kiosk.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(new MyWebSocketHandler(chatRepository, memberRepository, objectMapper), "/chat")
                .addInterceptors(new MyHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}