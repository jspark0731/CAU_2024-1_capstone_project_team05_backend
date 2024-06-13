package com.example.capstone.Service;

import com.example.capstone.Dto.MLDto;
import com.example.capstone.Dto.ResponseDto;
import com.example.capstone.Entity.MLEntity;
import com.example.capstone.Repository.MLRepository;
import com.example.capstone.Security.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class MLService
{
        private static final Logger logger = LoggerFactory.getLogger(MLService.class);

        @Autowired
        private MLRepository mlRepository;

        @Autowired
        private TokenProvider tokenProvider;

        public String getCurrentUserEmail(String token)
        {
                return tokenProvider.validateJwt(token);
        }

        public String defineFilePath(String email, String fileName)
        {
                String saveDirPath = "/spleeter_input/" + email;
                String saveFilePath = saveDirPath + "/" + fileName;
                logger.info("Save Directory Path: {}", saveDirPath);
                logger.info("Save File Path: {}", saveFilePath);

                File saveDir = new File(saveDirPath);
                if (!saveDir.exists())
                {
                        saveDir.mkdirs();
                }
                return saveFilePath;
        }

        public ResponseDto<?> saveFileAndData(MLDto mlDto, MultipartFile file)
        {
                try
                {
                        String parsedEmail = mlDto.getEmail().split("@")[0];
                        logger.info("Current User Email: {}", parsedEmail);

                        String saveFilePath = defineFilePath(parsedEmail, file.getOriginalFilename());
                        File saveFile = new File(saveFilePath);

                        // 파일이 이미 존재하는 경우 로그를 출력하고 덮어쓰기
                        if (saveFile.exists()) {
                                logger.info("File already exists and will be overwritten: {}", saveFilePath);
                        }

                        file.transferTo(saveFile);

                        // Database에 경로 저장 -> DB에 이미 있다면 작성 안한다?
                        mlDto.setFileName(file.getOriginalFilename());
                        mlDto.setFilePath(saveFilePath);
                        MLEntity mlEntity = new MLEntity(mlDto, mlDto.getEmail());

                        logger.info("Saving MLEntity to database: {}", mlEntity);
                        mlRepository.save(mlEntity);
                        logger.info("MLEntity After save");

                        // 데이터베이스 저장 후 생성된 ID를 DTO에 설정
                        mlDto.setId(mlEntity.getId());

                        return ResponseDto.setSuccessData("File uploaded and processed successfully.", saveFilePath);
                } catch (Exception e)
                {
                        return ResponseDto.setFailed("File upload failed: " + e.getMessage());
                }
        }

        public void sendMLDtoToFastApi(MLDto mlDto) throws Exception
        {
                String fastApiUrl = "http://machine-learning:5000/api/ml/ml_dto";

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<MLDto> request = new HttpEntity<>(mlDto, headers);

                ResponseEntity<String> response;

                try
                {
                        response = restTemplate.postForEntity(fastApiUrl, request, String.class);
                        if (!response.getStatusCode().is2xxSuccessful())
                        {
                                logger.error("Failed to send MLDto to FastAPI server. Status code: " + response.getStatusCode() + ", Response: " + response.getBody());
                                throw new Exception("Failed to send MLDto to FastAPI server. Status code: " + response.getStatusCode());
                        }
                } catch (Exception e)
                {
                        logger.error("Error sending MLDto to FastAPI server: " + e.getMessage(), e);
                        throw new Exception("Failed to send MLDto to FastAPI server", e);
                }
        }
}
