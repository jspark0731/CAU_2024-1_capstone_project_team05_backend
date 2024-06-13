package com.example.capstone.Service;

import com.example.capstone.Dto.ResponseDto;
import com.example.capstone.Dto.SheetDto;
import com.example.capstone.Entity.SheetEntity;
import com.example.capstone.Repository.SheetRepository;
import com.example.capstone.Security.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class SheetService
{
        private static final Logger logger = LoggerFactory.getLogger(SheetService.class);

        @Autowired
        private MLService mlService;

        @Autowired
        private SheetRepository sheetRepository;

        @Autowired
        private TokenProvider tokenProvider;

        public String getCurrentUserEmail(String token)
        {
                return tokenProvider.validateJwt(token);
        }

        public ResponseDto<?> downloadMp3FromApi(SheetDto sheetDto) {
                String videoId = sheetDto.getVideoId();
                String email = sheetDto.getEmail();
                String parsedEmail = email.split("@")[0];

                String downloadUrl = createDownloadUrl(videoId);
                RestTemplate restTemplate = new RestTemplate();

                try
                {
                        ResponseEntity<Resource> response = restTemplate.getForEntity(downloadUrl, Resource.class);

                        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null)
                        {
                                try (InputStream inputStream = response.getBody().getInputStream())
                                {
                                        Path targetDirectory = Paths.get("/music/" + parsedEmail);
                                        Files.createDirectories(targetDirectory); // 경로에 디렉터리가 있는지 확인하고, 없으면 생성

                                        Path targetPath = targetDirectory.resolve(videoId + ".mp3");
                                        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

                                        sheetDto.setFilePath(targetPath.toString());
                                        sheetDto.setFileName(videoId + ".mp3");
                                        SheetEntity sheetEntity = new SheetEntity(sheetDto, sheetDto.getEmail());
                                        sheetRepository.save(sheetEntity);

                                        sheetDto.setId(sheetEntity.getId());

                                        return ResponseDto.setSuccessData("Download and Save mp3 file successfully.", targetPath.toString());
                                } catch (IOException e)
                                {
                                        // Handle IOException from InputStream and Files.copy
                                        return ResponseDto.setFailed("Failed to save mp3 file: " + e.getMessage());
                                }
                        } else
                        {
                                return ResponseDto.setFailed("Failed to retrieve mp3 file. HTTP Status: " + response.getStatusCode());
                        }
                } catch (RestClientException e)
                {
                        // Handle exceptions related to the REST call
                        return ResponseDto.setFailedData("REST client exception: ", e.getMessage());
                }
        }

        public String createDownloadUrl(String videoId) {
                try {
                        String encodedVideoId = URLEncoder.encode(videoId, StandardCharsets.UTF_8.toString());
                        String url = String.format("https://api.283.kr/v1/tracks/download?track_id=%s", encodedVideoId);
                        logger.debug("Generated download URL: " + url);  // 로깅 추가
                        return url;
                } catch (Exception e) {
                        logger.error("Error encoding videoId: " + e.getMessage(), e);  // 에러 로깅
                        return null;
                }
        }

        public void sendSheetDtoToApi(SheetDto sheetDto) throws Exception
        {
                String fastApiUrl = "http://machine-learning:5000/api/ml/sheet_dto";

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<SheetDto> request = new HttpEntity<>(sheetDto, headers);

                ResponseEntity<String> response;

                try
                {
                        response = restTemplate.postForEntity(fastApiUrl, request, String.class);
                        if (!response.getStatusCode().is2xxSuccessful())
                        {
                                logger.error("Failed to send SheetDto to FastAPI server. Status code: " + response.getStatusCode() + ", Response: " + response.getBody());
                                throw new Exception("Failed to send SheetDto to FastAPI server. Status code: " + response.getStatusCode());
                        }
                } catch (Exception e)
                {
                        logger.error("Error sending SheetDto to FastAPI server: " + e.getMessage(), e);
                        throw new Exception("Failed to send SheetDto to FastAPI server", e);
                }
        }
}
