package com.petMed.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class RedirectLoggedInUsers implements HandlerInterceptor {

    private final Set<String> forbiddenPages = Set.of(
            "/login",
            "/register",
            "/register-owner",
            "/register-vet"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isLoggedIn = auth != null && !auth.getPrincipal().equals("anonymousUser");

        if (isLoggedIn && forbiddenPages.contains(request.getRequestURI())) {
            response.sendRedirect("/home");
            return false;
        }

        return true;
    }
}
