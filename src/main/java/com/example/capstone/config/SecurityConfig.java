package com.example.capstone.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
        @Autowired
        private CustomAuthenticationSuccessHandler successHandler;

        @Autowired
        private CustomAuthenticationFailureHandler failureHandler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
        {
                // authorized url setting
                // /login, /signup page is accepted to all, other pages can be accepted to authorized people
                http
                        .csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(authz -> authz
                                .requestMatchers("/api/","/api/sign-in", "/api/sign-up", "/api/auth").permitAll()
                                .requestMatchers("/api/logout").authenticated()
                                .anyRequest().permitAll()
                        );

                // sign-in setting
                http
                        .formLogin((formLogin) ->
                                formLogin
                                        .loginPage("/api/sign-in")    // 로그인 페이지 URL
                                        .loginProcessingUrl("/api/auth")    // 로그인 처리(POST 요청 처리) URL
                                        .usernameParameter("email")
                                        .passwordParameter("password")
                                        .successHandler(successHandler)
                                        .failureHandler(failureHandler)
                                        .permitAll()
                        );

                // logout setting
                http
                        .logout((logoutConfig)->
                                logoutConfig.logoutUrl("/api/logout")
                                .logoutSuccessUrl("/")	// redirect "/" after logout
                        );

                return http.build();
        }
}
