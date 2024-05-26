package com.example.capstone.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class MLDto
{
        private String token;
        private MultipartFile file;
        private String model;
        private String type;
}
