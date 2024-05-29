package com.example.capstone.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MLDto
{
        private String token;
        private String model;
        private String instrumentType;
        private String fileName;
        private String filePath;
}
