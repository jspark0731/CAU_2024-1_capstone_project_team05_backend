package com.example.capstone.security;

import com.example.capstone.config.CustomAuthenticationFailureHandler;
import com.example.capstone.service.UserService;
import com.example.capstone.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthProvider implements AuthenticationProvider {
        @Autowired
        private UserService userService;

        private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException
        {
                String email = (String) authentication.getPrincipal(); // 로그인 창에 입력한 email
                String password = (String) authentication.getCredentials(); // 로그인 창에 입력한 password

                Object principal = authentication.getPrincipal();
                Object credentials = authentication.getCredentials();
                logger.info("Principal (Email): {}", principal);
                logger.info("Credentials (Password): {}", credentials);
                logger.info("Authentication object: {}", authentication);

                logger.info("Before Authenticating user with email: {}, password : {}", email, password);

                // PasswordEncoder passwordEncoder = userService.passwordEncoder();
                UsernamePasswordAuthenticationToken token;
                UserVo userVo = userService.getUserByEmail(email);

                //if (userVo != null && passwordEncoder.matches(password, userVo.getPassword()))
                if (userVo != null && userVo.getPassword().equals(password))
                { // 일치하는 user 정보가 있는지 확인
                        List<GrantedAuthority> roles = new ArrayList<>();
                        roles.add(new SimpleGrantedAuthority("USER")); // 권한 부여

                        token = new UsernamePasswordAuthenticationToken(userVo.getId(), null, roles);
                        // 인증된 user 정보를 담아 SecurityContextHolder에 저장되는 token

                        return token;
                }

                throw new BadCredentialsException("After Auth. No such user or wrong password. email: {" + email +  "}" + "password: {" + password + "}");
                // Exception을 던지지 않고 다른 값을 반환하면 authenticate() 메서드는 정상적으로 실행된 것이므로 인증되지 않았다면 Exception을 throw 해야 한다.
        }

        @Override
        public boolean supports(Class<?> authentication)
        {
                boolean supports = UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
                logger.info("Supports class {}: {}", authentication.getSimpleName(), supports);
                return supports;
        }
}
