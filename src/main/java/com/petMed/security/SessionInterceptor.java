package com.petMed.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    public static final Set<String> UNAUTHENTICATED_ENDPOINTS = Set.of("/", "/login", "/register", "/error");
    public static final String USER_ID_FROM_SESSION = "user_id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String endpoint = request.getServletPath();
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = session != null && session.getAttribute(USER_ID_FROM_SESSION) != null;

        if (!isLoggedIn && !UNAUTHENTICATED_ENDPOINTS.contains(endpoint)) {
            response.sendRedirect("/");
            return false;
        }

        if (isLoggedIn && UNAUTHENTICATED_ENDPOINTS.contains(endpoint)) {
            response.sendRedirect("/home");
            return false;
        }

        return true;
    }
}
