package com.example.capstone.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
// @AllArgsConstructor
@NoArgsConstructor
public class SignUpDto
{
        private String email;
        private String username;
        private String name;
        private String password;
        private LocalDateTime createdAt;
        private LocalDateTime editedAt;
        private LocalDateTime lastLoginAt;
}