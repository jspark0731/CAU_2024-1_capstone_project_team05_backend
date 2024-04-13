package com.example.capstone.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException exception) throws IOException, ServletException
        {
                // 로그인 실패 시 수행할 로직 (예: 에러 메시지 설정)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().print("Authentication failed: " + exception.getMessage());
        }
}
