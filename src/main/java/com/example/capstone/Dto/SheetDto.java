package com.example.capstone.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SheetDto
{
        private int id;
        private String token;
        private String email;
        private String videoId;
        private String instrumentType;
        private String model;
        private String filePath;
        private String fileName;
        private String spleeterOutputPath;
        private String basicPitchOutputPath;
        private String musescoreOutputPath;
}
