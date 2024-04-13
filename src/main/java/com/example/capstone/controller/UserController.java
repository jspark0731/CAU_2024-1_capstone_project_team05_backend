package com.example.capstone.controller;


import com.example.capstone.service.UserService;
import com.example.capstone.vo.UserVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api")
public class UserController
{
        @Autowired
        private UserService userService;

        @GetMapping("/")
        public String serveFrontendApp() {
                return "forward:/";
        }

        @GetMapping("/sign-in")
        public String signInPage()
        { // processing request sign-in page
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication instanceof AnonymousAuthenticationToken) {
                        return "redirect:/api/sign-up"; // 여기서 "signInPage"는 실제로 프론트엔드 라우트로 대체되어야 한다.
                }
                return "redirect:/"; // 이미 인증된 사용자는 홈으로 리디렉션
        }

        @GetMapping("/sign-up")
        public String signupPage()
        {  // 회원 가입 페이지
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication instanceof AnonymousAuthenticationToken) {
                        return "redirect:/api/sign-up";
                }
                return "redirect:/"; // redirect to "/" pre-authenticated
        }

        @PostMapping("/sign-up")
        public String signup(@RequestBody UserVo userVo, RedirectAttributes redirectAttributes)
        {
                try {
                        userService.signup(userVo);
                        return "redirect:/sign-in"; // redirect to "/" success to auth
                } catch (DuplicateKeyException e) {
                        // 중복 ID 에러 처리
                        redirectAttributes.addFlashAttribute("error", "Username is already in use");
                        return "redirect:/sign-up"; // 에러 메시지와 함께 회원가입 페이지로 리디렉션
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("error", "An unexpected error occurred");
                        return "redirect:/sign-up"; // 다른 예외 발생 시
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