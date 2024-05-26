package com.example.capstone.Entity;

import com.example.capstone.Dto.SignUpDto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="User")
public class UserEntity
{
        @Id
        private String email;
        private String password;
        private String name;
        private String userType;
        private LocalDateTime createdAt;
        private LocalDateTime editedAt;
        private LocalDateTime lastLoginAt;

        public UserEntity(SignUpDto dto)
        {
                this.email = dto.getEmail();
                this.password = dto.getPassword();
                this.name = dto.getName();
                this.userType = dto.getUserType();
                this.createdAt = LocalDateTime.now();
                this.editedAt = LocalDateTime.now();
        }
}
