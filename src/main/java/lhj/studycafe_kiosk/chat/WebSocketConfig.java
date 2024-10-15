package lhj.studycafe_kiosk.chat;

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

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(new MyWebSocketHandler(chatRepository, memberRepository), "/chat")
                .addInterceptors(new MyHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}