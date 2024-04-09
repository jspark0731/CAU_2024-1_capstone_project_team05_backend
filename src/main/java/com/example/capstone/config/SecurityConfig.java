package com.example.capstone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
        {
                // authorized url setting
                // /login, /signup page is accepted to all, other pages can be accepted to authorized people
                http
                        .authorizeHttpRequests(authz -> authz
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().permitAll()
                        );
//                // login setting
//                http
//                        .formLogin((formLogin)->
//                                formLogin
//                                        .loginPage("/login")    // GET 요청 (login form을 보여줌)
//                                        .loginProcessingUrl("/auth")    // POST 요청 (login 창에 입력한 데이터를 처리)
//                                        .usernameParameter("email")	// login에 필요한 id 값을 email로 설정 (default는 username)
//                                        .passwordParameter("password")	// login에 필요한 password 값을 password(default)로 설정
//                                        .defaultSuccessUrl("/")	// login에 성공하면 /로 redirect
//                        );
//                // logout setting
//                http
//                        .logout((logoutConfig)->
//                                logoutConfig.logoutUrl("/logout")
//                                .logoutSuccessUrl("/")	// logout에 성공하면 /로 redirect
//                        );
                return http.build();
        }
}