package lhj.studycafe_kiosk.chat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class MyHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        HttpHeaders headers = request.getHeaders();
        List<String> cookies = headers.get("cookie");
        if (cookies == null) {
            return false;
        }

        for (String cookie : cookies) {
            if (cookie.contains("JSESSIONID")) {

                String jsessionId = (cookie.split("="))[1];

                if (jsessionId != null) {
                    HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
                    HttpSession session = servletRequest.getSession(false);
                    if (session != null && jsessionId.equals(session.getId())) {
                        Long memberId = (Long) session.getAttribute("loginMember");
                        attributes.put("memberId", memberId);
                    }
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
