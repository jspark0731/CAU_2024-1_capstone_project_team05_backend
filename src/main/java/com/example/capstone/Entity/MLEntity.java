package com.example.capstone.Entity;

import com.example.capstone.Dto.MLDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="Music")
public class MLEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String model;
        private String instrumentType;
        private String fileName;
        private String filePath;

        @Column(name = "userEmail")
        private String userEmail;

        @Column(name = "spleeterOutputPath")
        private String spleeterOutputPath;

        @Column(name = "basicPitchOutputPath")
        private String basicPitchOutputPath;

        @Column(name = "musescoreOutputPath")
        private String musescoreOutputPath;

        public MLEntity(MLDto dto, String userEmail) {
                this.model = dto.getModel();
                this.instrumentType = dto.getInstrumentType();
                this.fileName = dto.getFileName();
                this.filePath = dto.getFilePath();
                this.userEmail = userEmail;
                this.spleeterOutputPath = dto.getSpleeterOutputPath();
                this.basicPitchOutputPath = dto.getBasicPitchOutputPath();
                this.musescoreOutputPath = dto.getMusescoreOutputPath();
        }
}
