package com.example.capstone.Entity;

import com.example.capstone.Dto.SheetDto;
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
@Table(name = "Sheet")
public class SheetEntity
{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(name = "userEmail")
        private String userEmail;

        private String videoId;
        private String instrumentType;
        private String model;
        private String filePath;
        private String fileName;

        @Column(name = "spleeterOutputPath")
        private String spleeterOutputPath;

        @Column(name = "basicPitchOutputPath")
        private String basicPitchOutputPath;

        @Column(name = "musescoreOutputPath")
        private String musescoreOutputPath;

        public SheetEntity(SheetDto sheetDto, String userEmail)
        {
                this.videoId = sheetDto.getVideoId();
                this.instrumentType = sheetDto.getInstrumentType();
                this.model = sheetDto.getModel();
                this.userEmail = userEmail;
                this.filePath = sheetDto.getFilePath();
                this.fileName = sheetDto.getFileName();
                this.spleeterOutputPath = sheetDto.getSpleeterOutputPath();
                this.basicPitchOutputPath = sheetDto.getBasicPitchOutputPath();
                this.musescoreOutputPath = sheetDto.getMusescoreOutputPath();
        }
}
