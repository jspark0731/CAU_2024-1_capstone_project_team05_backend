package com.example.capstone.Entity;

import com.example.capstone.Dto.SignUpDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
        private LocalDateTime createdAt;
        private LocalDateTime editedAt;
        private LocalDateTime lastLoginAt;

        @OneToMany(mappedBy = "userEmail")
        private List<MLEntity> mlEntities;

        @OneToMany(mappedBy = "userEmail")
        private List<SheetEntity> sheetEntities;

        public UserEntity(SignUpDto dto)
        {
                this.email = dto.getEmail();
                this.password = dto.getPassword();
                this.name = dto.getName();
                this.createdAt = LocalDateTime.now();
                this.editedAt = LocalDateTime.now();
        }
}
