package com.example.capstone.controller;


import com.example.capstone.service.UserService;
import com.example.capstone.vo.UserVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
        @Autowired
        private UserService userService;

        @GetMapping("/path/{pathVariable}")
        public String redirectToFrontendApp(@PathVariable String pathVariable) {
                return "forward:/";
        }

        @GetMapping("/")
        public String home(Model model)
        { // 인증된 사용자의 정보를 보여줌
                Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                // token에 저장되어 있는 인증된 사용자의 id 값

                UserVo userVo = userService.getUserById(id);
                userVo.setPassword(null); // password는 보이지 않도록 null로 설정
                model.addAttribute("user", userVo);
                return "home";
        }

        @GetMapping("/userList")
        public String getUserList(Model model)
        { // User 테이블의 전체 정보를 보여줌
                List<UserVo> userList = userService.getUserList();
                model.addAttribute("list", userList);
                return "userListPage";
        }

        @GetMapping("/login")
        public String loginPage()
        { // 로그인되지 않은 상태이면 로그인 페이지를, 로그인된 상태이면 home 페이지를 보여줌
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication instanceof AnonymousAuthenticationToken)
                        return "loginPage";
                return "redirect:/";
        }

        @GetMapping("/signup")
        public String signupPage()
        {  // 회원 가입 페이지
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication instanceof AnonymousAuthenticationToken)
                        return "signupPage";
                return "redirect:/";
        }

        @PostMapping("/signup")
        public ResponseEntity<?> signup(@RequestBody UserVo userVo)
        {
                try {
                        userService.signup(userVo);
                        return ResponseEntity.ok("User created successfully.");
                } catch (DuplicateKeyException e) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is already in use.");
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
                }
        }

        @GetMapping("/update")
        public String editPage(Model model)
        { // 회원 정보 수정 페이지
                Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                UserVo userVo = userService.getUserById(id);
                model.addAttribute("user", userVo);
                return "editPage";
        }

        @PostMapping("/update")
        public String edit(UserVo userVo)
        { // 회원 정보 수정
                Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                userVo.setId(id);
                userService.edit(userVo);
                return "redirect:/";
        }

        @PostMapping("/delete")
        public String withdraw(HttpSession session)
        { // 회원 탈퇴
                Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (id != null) {
                        userService.withdraw(id);
                }
                SecurityContextHolder.clearContext(); // SecurityContextHolder에 남아있는 token 삭제
                return "redirect:/";
        }
}