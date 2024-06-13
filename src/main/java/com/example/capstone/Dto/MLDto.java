package com.example.capstone.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MLDto
{
        private String token;
        private int id;
        private String model;
        private String instrumentType;
        private String fileName;
        private String filePath;
        private String email; // foreign key in User Table
        private String spleeterOutputPath;
        private String basicPitchOutputPath;
        private String musescoreOutputPath;
}
