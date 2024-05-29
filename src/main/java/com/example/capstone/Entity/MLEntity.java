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
        private Long id;
        private String model;
        private String instrumentType;
        private String fileName;
        private String filePath;

        public MLEntity(MLDto dto) {
                this.model = dto.getModel();
                this.instrumentType = dto.getInstrumentType();
                this.fileName = dto.getFileName();
                this.filePath = dto.getFilePath();
        }
}
