package com.example.capstone.Entity;

import com.example.capstone.Dto.SignUpDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="User")				// 본인 테이블명과 맞춰주어야 함
public class UserEntity {
        @Id
        private String email;
        private String password;
        private String name;
        private String userType;
        private String token;
        private LocalDateTime createdAt;
        private LocalDateTime editedAt;
        private LocalDateTime lastLoginAt;

        public UserEntity(SignUpDto dto) {
                this.email = dto.getEmail();
                this.password = dto.getPassword();
                this.name = dto.getName();
                this.userType = dto.getUserType();
                this.token = "";
                this.createdAt = LocalDateTime.now();
                this.editedAt = LocalDateTime.now();
        }
}
