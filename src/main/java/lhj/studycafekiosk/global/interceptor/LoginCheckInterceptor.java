package lhj.studycafekiosk.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        if (session == null) {
//            String requestURI = request.getRequestURI();
//            response.sendRedirect("/members/login?redirectURL=" + requestURI);
//            return false;
        }
        return true;
    }
}
