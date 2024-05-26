package com.example.capstone.Service;

import com.example.capstone.Dto.MLDto;
import com.example.capstone.Dto.ResponseDto;
import com.example.capstone.Entity.MLEntity;
import com.example.capstone.Repository.UserRepository;
import com.example.capstone.Security.TokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class MLService {
        private static final Logger logger = LoggerFactory.getLogger(MLService.class);

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private TokenProvider tokenProvider;

        public String getCurrentUserEmail(String token) {
                return tokenProvider.validateJwt(token);
        }

        public ResponseDto<?> fileUploadToMLContainer(MLDto mlDto, MultipartFile file) {
                try {
                        String email = getCurrentUserEmail(mlDto.getToken()).split("@")[0];
                        logger.info("Current User Email: {}", email);

                        String saveDirPath = "/spleeter_input/" + email;
                        String saveFilePath = saveDirPath + "/" + file.getOriginalFilename();

                        File saveDir = new File(saveDirPath);
                        if (!saveDir.exists()) {
                                saveDir.mkdirs();
                        }

                        File saveFile = new File(saveFilePath);
                        file.transferTo(saveFile);

                        // Database에 경로 저장
                        MLEntity mlEntity = new MLEntity(mlDto);
                        mlEntity.setFilePath(saveFilePath);
                        // 저장 로직 추가

                        return ResponseDto.setSuccessData("File uploaded and processed successfully.", saveFilePath);
                } catch (Exception e) {
                        return ResponseDto.setFailed("File upload failed: " + e.getMessage());
                }
        }

        public void sendFilePathToFastApi(String filePath) throws Exception {
                String fastApiUrl = "http://localhost:5000/api/ML/fileProcess"; // FastAPI 서버의 URL

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                Map<String, String> requestBody = new HashMap<>();
                requestBody.put("filePath", filePath);

                HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl, request, String.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                        throw new Exception("Failed to send file path to FastAPI server");
                }
        }
}
