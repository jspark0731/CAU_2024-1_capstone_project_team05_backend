package com.example.capstone.Service;

import com.example.capstone.Dto.ResponseDto;
import com.example.capstone.Dto.SignInDto;
import com.example.capstone.Dto.SignInResponseDto;
import com.example.capstone.Dto.SignUpDto;
import com.example.capstone.Entity.UserEntity;
import com.example.capstone.Repository.UserRepository;
import com.example.capstone.Security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService
{
        @Autowired
        UserRepository userRepository;

        @Autowired
        TokenProvider tokenProvider;

        public ResponseDto<?> signUp(SignUpDto dto)
        {
                String email = dto.getEmail();
                String password = dto.getPassword();

                // email(id) 중복 확인
                try
                {
                        // 존재하는 경우 : true / 존재하지 않는 경우 : false
                        if(userRepository.existsById(email))
                        {
                                return ResponseDto.setFailed("중복된 Email 입니다.");
                        }
                } catch (Exception e)
                {
                        return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
                }

                // UserEntity 생성
                UserEntity userEntity = new UserEntity(dto);

                // 비밀번호 암호화
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String hashedPassword = passwordEncoder.encode(password);

                boolean isPasswordMatch = passwordEncoder.matches(password, hashedPassword);

                if(!isPasswordMatch)
                {
                        return ResponseDto.setFailed("암호화에 실패하였습니다.");
                }

                userEntity.setPassword(hashedPassword);

                // UserRepository를 이용하여 DB에 Entity 저장
                try
                {
                        userRepository.save(userEntity);
                } catch (Exception e)
                {
                        return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
                }

                return ResponseDto.setSuccess("회원 생성에 성공했습니다.");
        }

        public ResponseDto<SignInResponseDto> signIn(SignInDto dto)
        {
                String email = dto.getEmail();
                String password = dto.getPassword();
                UserEntity userEntity;

                try
                {
                        // 이메일로 사용자 정보 가져오기
                        userEntity = userRepository.findById(email).orElse(null);
                        if(userEntity == null)
                        {
                                return ResponseDto.setFailed("입력하신 이메일로 등록된 계정이 존재하지 않습니다.");
                        }

                        // 사용자가 입력한 비밀번호를 BCryptPasswordEncoder를 사용하여 암호화
                        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                        String encodedPassword = userEntity.getPassword();

                        // 저장된 암호화된 비밀번호와 입력된 암호화된 비밀번호 비교
                        if(!passwordEncoder.matches(password, encodedPassword))
                        {
                                return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
                        }
                } catch (Exception e)
                {
                        return ResponseDto.setFailed("데이터베이스 연결에 실패하였습니다.");
                }

                // Client에 비밀번호 제공 방지
                userEntity.setPassword("");

                int exprTime = 7200000;     // 2h
                String token = tokenProvider.createJwt(email, exprTime);

                SignInResponseDto signinResponseDto = new SignInResponseDto(token, exprTime, userEntity);

                return ResponseDto.setSuccessData("로그인에 성공하였습니다.", signinResponseDto);
        }

}