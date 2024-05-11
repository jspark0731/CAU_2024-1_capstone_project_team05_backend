package com.example.capstone.Dto;

import com.example.capstone.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponseDto
{
        private String token;
        private int exprTime;
        private UserEntity user;
}
