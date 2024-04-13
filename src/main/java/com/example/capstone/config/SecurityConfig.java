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
                                .requestMatchers("/api/","/api/sign-in", "/api/sign-up", "/api/logout").permitAll()
                                .anyRequest().permitAll()
                        );
                // sign-in setting
                http
                        .formLogin((formLogin)->
                                formLogin
                                        .loginPage("/api/sign-in")    // GET Request (show sign-in form)
                                        .loginProcessingUrl("/api/auth")    // POST Request (process data in sign-in page)
                                        .usernameParameter("email")	// setting default id value for sign-in email
                                        .passwordParameter("password")	// password needed for sign-in
                                        .defaultSuccessUrl("/", true)	// login에 성공하면 /로 redirect
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