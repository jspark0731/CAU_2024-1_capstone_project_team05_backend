package com.example.capstone.Controller;

import com.example.capstone.Dto.ResponseDto;
import com.example.capstone.Dto.SignInDto;
import com.example.capstone.Dto.SignInResponseDto;
import com.example.capstone.Dto.SignUpDto;
import com.example.capstone.Service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
        @Autowired
        private AuthService authService;

        @PostMapping("/sign-in")
        public ResponseEntity<?> signIn(@RequestBody SignInDto requestBody)
        {
                ResponseDto<?> result = authService.signIn(requestBody);
                return setToken(result);
        }


        // Response 결과에 따라 Header에 Token 설정
        private ResponseEntity<?> setToken(ResponseDto<?> result)
        {
                // 요청이 성공인 경우
                if(result.getResult())
                {
                        // result -> data -> token 추출
                        SignInResponseDto signInResponse = (SignInResponseDto) result.getData();

                        // Header에 Auth에 Token 지정, Body에는 result 그대로 작성 (result 내의 token은 제거해도 될듯)
                        return ResponseEntity.ok()
                                .header("Authorization", "Bearer " + signInResponse.getToken())
                                .body(result);
                } else
                {
                        return ResponseEntity.status(404).body(result);
                }
        }

        // 회원가입 처리
        @PostMapping("/sign-up")
        public ResponseDto<?> signUp(@RequestBody SignUpDto requestBody)
        {
                ResponseDto<?> result = authService.signUp(requestBody);
                return result;
        }

//        // 사용자 정보 조회
//        @GetMapping("/user/info")
//        public ResponseEntity<?> getUserInfo(Authentication authentication)
//        {
//                if (authentication == null || !authentication.isAuthenticated())
//                {
//                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
//                }
//                UserVo userVo = userService.getUserByEmail(authentication.getName());
//                if (userVo == null)
//                {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//                }
//                return ResponseEntity.ok(userVo);
//        }
//
//        // 사용자 정보 수정
//        @PutMapping("/user/update")
//        public ResponseEntity<?> updateUser(@RequestBody UserVo userVo, Authentication authentication) {
//                if (!authentication.getName().equals(userVo.getEmail()))
//                {
//                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own information");
//                }
//                try
//                {
//                        userService.edit(userVo);
//                        return ResponseEntity.ok("User updated successfully");
//                } catch (Exception e)
//                {
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the user");
//                }
//        }
//
//        // 사용자 탈퇴
//        @DeleteMapping("/user/delete")
//        public ResponseEntity<?> deleteUser(Authentication authentication, @RequestBody UserVo userVo) {
//                try {
//                        userService.withdraw(authentication.getName().equals(userVo.getId()));
//                        SecurityContextHolder.clearContext(); // Clear Security Context
//                        return ResponseEntity.ok("User deleted successfully");
//                } catch (Exception e) {
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the user");
//                }
//        }
}
